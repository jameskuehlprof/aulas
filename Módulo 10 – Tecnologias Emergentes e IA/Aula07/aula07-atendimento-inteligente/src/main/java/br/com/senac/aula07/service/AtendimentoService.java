package br.com.senac.aula07.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.senac.aula07.config.IaProperties;
import br.com.senac.aula07.dto.Dtos.MetricasResponse;
import br.com.senac.aula07.dto.Dtos.PerguntaResponse;
import br.com.senac.aula07.ia.IaException;
import br.com.senac.aula07.ia.IaProvider;
import br.com.senac.aula07.ia.IaProviderFactory;
import br.com.senac.aula07.ia.MensagemChat;
import br.com.senac.aula07.ia.RespostaIA;
import br.com.senac.aula07.model.Pergunta;
import br.com.senac.aula07.model.Usuario;
import br.com.senac.aula07.repository.PerguntaRepository;

/**
 * Coracao do sistema: e AQUI que a IA e chamada (nunca no Controller).
 *
 * Fluxo de POST /api/perguntas:
 *   1) valida o usuario (UsuarioService)
 *   2) normaliza o texto e consulta o CACHE (Desafio)
 *   3) monta o contexto: system prompt + ultimas N perguntas/respostas do usuario
 *   4) chama a IA; se falhar, responde com FALLBACK (o sistema nao cai)
 *   5) persiste a Pergunta com origem, tokens e tempo (Exercicio 2)
 *   6) (bonus) classifica a pergunta em uma categoria
 */
@Service
public class AtendimentoService {

    private static final Logger log = LoggerFactory.getLogger(AtendimentoService.class);

    static final String SYSTEM_PROMPT = """
            Voce e a "Lia", assistente virtual da TechFix, assistencia tecnica de celulares
            e notebooks em Blumenau/SC. Tom simpatico e objetivo, no maximo 4 frases.
            Voce so responde sobre servicos da TechFix (troca de tela, bateria, formatacao,
            orcamento gratuito feito na loja, prazos, horario 8h-18h seg-sex, pagamento
            Pix/cartao/dinheiro) e cuidados basicos com aparelhos. Fora disso, responda:
            "Desculpe, so consigo ajudar com assuntos da TechFix e cuidados com seu aparelho."
            Nunca invente precos.
            """;

    static final String RESPOSTA_FALLBACK = "No momento nao consigo responder automaticamente. "
            + "Sua pergunta foi registrada e nossa equipe vai retornar em breve. "
            + "Se preferir, ligue para (47) 3333-0000.";

    private final UsuarioService usuarios;
    private final PerguntaRepository perguntas;
    private final CacheService cache;
    private final IaProviderFactory fabrica;
    private final IaProperties props;

    public AtendimentoService(UsuarioService usuarios, PerguntaRepository perguntas, CacheService cache,
                              IaProviderFactory fabrica, IaProperties props) {
        this.usuarios = usuarios;
        this.perguntas = perguntas;
        this.cache = cache;
        this.fabrica = fabrica;
        this.props = props;
    }

    // ------------------------------------------------------------ Exercicio 1 + 2 + Desafio
    @Transactional
    public PerguntaResponse perguntar(Long usuarioId, String texto) {
        Usuario usuario = usuarios.buscarEntidade(usuarioId);
        String normalizado = CacheService.normalizar(texto);
        Pergunta pergunta = new Pergunta(usuario, texto.trim(), normalizado);

        // 2) cache (o tempo tambem e medido, para comparar com a IA nas metricas)
        long inicio = System.currentTimeMillis();
        Optional<Pergunta> emCache = cache.buscar(normalizado);
        if (emCache.isPresent()) {
            Pergunta origem = emCache.get();
            pergunta.registrarResposta(origem.getResposta(), Pergunta.Origem.CACHE,
                    origem.getProvedor(), origem.getModelo(), 0, 0, System.currentTimeMillis() - inicio);
            pergunta.setCategoria(origem.getCategoria());
            return PerguntaResponse.de(perguntas.save(pergunta));
        }

        // 3) contexto + 4) IA com fallback
        IaProvider ia = fabrica.obter(props.provedor());
        try {
            RespostaIA r = ia.conversar(montarHistorico(usuarioId, texto), SYSTEM_PROMPT);
            pergunta.registrarResposta(r.texto(), Pergunta.Origem.IA, r.provedor(), r.modelo(),
                    r.tokensEntrada(), r.tokensSaida(), r.tempoMs());
            pergunta.setCategoria(classificar(ia, texto));
        } catch (IaException e) {
            // O erro da API externa NAO derruba o sistema: registramos e respondemos com fallback.
            log.error("IA indisponivel ({}): {}", e.getStatusHttp(), e.getMessage());
            pergunta.registrarResposta(RESPOSTA_FALLBACK, Pergunta.Origem.FALLBACK, ia.nome(), null,
                    0, 0, System.currentTimeMillis() - inicio);
        }

        // 5) persistencia
        return PerguntaResponse.de(perguntas.save(pergunta));
    }

    /** Ultimas N interacoes REAIS do usuario, em ordem cronologica, + a pergunta atual. */
    private List<MensagemChat> montarHistorico(Long usuarioId, String perguntaAtual) {
        List<Pergunta> ultimas = new ArrayList<>(perguntas.findTop6ByUsuarioIdOrderByDataHoraDesc(usuarioId));
        Collections.reverse(ultimas);
        List<MensagemChat> historico = new ArrayList<>();
        for (Pergunta p : ultimas) {
            if (p.getOrigem() == Pergunta.Origem.FALLBACK || p.getResposta() == null) continue;
            historico.add(MensagemChat.usuario(p.getTexto()));
            historico.add(MensagemChat.assistente(p.getResposta()));
        }
        int max = props.historico().tamanho();
        while (historico.size() > max) historico.remove(0);
        historico.add(MensagemChat.usuario(perguntaAtual));
        return historico;
    }

    /** Bonus: classificacao em 1 palavra, com custo minimo (resposta de 1 token). */
    private String classificar(IaProvider ia, String texto) {
        try {
            String prompt = "Classifique a mensagem de um cliente de assistencia tecnica em UMA destas "
                    + "categorias: orcamento, prazo, garantia, suporte, outros. Responda so com a palavra.\n"
                    + "Mensagem: " + texto;
            String cat = ia.perguntar(prompt).texto().toLowerCase().replaceAll("[^a-z]", "");
            return List.of("orcamento", "prazo", "garantia", "suporte").contains(cat) ? cat : "outros";
        } catch (IaException e) {
            return null;
        }
    }

    // ------------------------------------------------------------ Exercicio 2
    public List<PerguntaResponse> historico(Long usuarioId) {
        usuarios.buscarEntidade(usuarioId);
        return perguntas.findByUsuarioIdOrderByDataHoraDesc(usuarioId).stream().map(PerguntaResponse::de).toList();
    }

    // ------------------------------------------------------------ Desafio: metricas
    public MetricasResponse metricas() {
        long ia = perguntas.countByOrigem(Pergunta.Origem.IA);
        long hits = perguntas.countByOrigem(Pergunta.Origem.CACHE);
        long fb = perguntas.countByOrigem(Pergunta.Origem.FALLBACK);
        long total = ia + hits + fb;
        double taxa = total == 0 ? 0 : Math.round(hits * 10000.0 / total) / 100.0;
        return new MetricasResponse(total, ia, hits, fb, taxa,
                Math.round(perguntas.tempoMedioPorOrigem(Pergunta.Origem.IA)),
                Math.round(perguntas.tempoMedioPorOrigem(Pergunta.Origem.CACHE)),
                perguntas.totalTokens(), cache.ativo());
    }
}
