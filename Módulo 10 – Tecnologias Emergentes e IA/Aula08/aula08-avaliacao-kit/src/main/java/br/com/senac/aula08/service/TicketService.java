package br.com.senac.aula08.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.senac.aula08.config.IaProperties;
import br.com.senac.aula08.dto.Dtos.MetricasResponse;
import br.com.senac.aula08.dto.Dtos.TicketResponse;
import br.com.senac.aula08.ia.IaProviderFactory;
import br.com.senac.aula08.repository.TicketRepository;

/**
 * =====================================================================
 *  AVALIACAO FINAL - "TechFix Inteligente" - KIT DO ALUNO
 * =====================================================================
 *
 * O que JA esta pronto (nao precisa mexer): entidades Usuario e Ticket,
 * repositories, provedores de IA (pacote ia/ - texto e imagem), UsuarioService,
 * UsuarioController, DTOs, GlobalExceptionHandler, pagina web e Swagger.
 *
 * O que VOCE implementa (ver "Exercicios e Desafios - Aula08.docx"):
 *
 *  Incremento 1 (Aulas 04/05) - abrirTicket(): chamar a IA com o SYSTEM_PROMPT
 *                               (fabrica.obter(props.provedor()).conversar(...)).
 *  Incremento 2 (Aula 07)     - persistir o Ticket (registrarResposta + save) e
 *                               implementar historico() e buscar().
 *  Incremento 3 (Aula 02)     - ClassificadorRegras (categoria + prioridade) e a
 *                               classificacao pela IA; gravar com ticket.classificar(...).
 *  Incremento 4 (Aula 06)     - anexarFoto(): validar a imagem e usar
 *                               perguntarComImagem(...) para descrever o problema.
 *  Incremento 5 (Aula 07)     - fallback (IaException -> RESPOSTA_FALLBACK),
 *                               CacheService e metricas().
 *  Incremento 6 (Aulas 01/03) - boas praticas: validacao, nenhuma chave no codigo,
 *                               Swagger, README com nota de etica/LGPD.
 *
 * Dica: comece pelo Incremento 1 e va testando no Swagger a cada passo.
 */
@Service
public class TicketService {

    private static final Logger log = LoggerFactory.getLogger(TicketService.class);

    // TODO Incremento 1: personalize a assistente (nome, empresa, escopo, recusa)
    static final String SYSTEM_PROMPT = """
            Voce e a assistente virtual da TechFix, assistencia tecnica de celulares e notebooks.
            ...
            """;

    static final String RESPOSTA_FALLBACK = "No momento nao consigo responder automaticamente. "
            + "Seu ticket foi registrado e nossa equipe vai retornar em breve.";

    private final UsuarioService usuarios;
    private final TicketRepository tickets;
    private final CacheService cache;
    private final ClassificadorRegras regras;
    private final IaProviderFactory fabrica;
    private final IaProperties props;

    public TicketService(UsuarioService usuarios, TicketRepository tickets, CacheService cache,
                         ClassificadorRegras regras, IaProviderFactory fabrica, IaProperties props) {
        this.usuarios = usuarios;
        this.tickets = tickets;
        this.cache = cache;
        this.regras = regras;
        this.fabrica = fabrica;
        this.props = props;
    }

    @Transactional
    public TicketResponse abrirTicket(Long usuarioId, String texto) {
        // TODO Incremento 1: validar usuario, chamar a IA, devolver TicketResponse
        // TODO Incremento 2: persistir (ticket.registrarResposta(...); tickets.save(ticket))
        // TODO Incremento 3: regras.categoria/prioridade + classificacao pela IA -> ticket.classificar(...)
        // TODO Incremento 5: cache.buscar(...) ANTES da IA; try/catch IaException -> FALLBACK
        throw new UnsupportedOperationException("Incremento 1 ainda nao implementado");
    }

    public List<TicketResponse> historico(Long usuarioId) {
        // TODO Incremento 2
        throw new UnsupportedOperationException("Incremento 2 ainda nao implementado");
    }

    public TicketResponse buscar(Long id) {
        // TODO Incremento 2 (404 com RecursoNaoEncontradoException se nao existir)
        throw new UnsupportedOperationException("Incremento 2 ainda nao implementado");
    }

    @Transactional
    public TicketResponse anexarFoto(Long ticketId, byte[] imagem, String mimeType) {
        // TODO Incremento 4: validar (vazia? > 8 MB? tipo JPEG/PNG/WEBP?), montar o prompt,
        //      chamar fabrica.obter(props.provedor()).perguntarComImagem(imagem, mimeType, prompt),
        //      gravar em ticket.setDescricaoAnexo(...). Se a IA falhar, gravar um aviso e nao quebrar.
        throw new UnsupportedOperationException("Incremento 4 ainda nao implementado");
    }

    public MetricasResponse metricas() {
        // TODO Incremento 5: usar countByOrigem, tempoMedioPorOrigem, totalTokens,
        //      countByPrioridade, concordanciasRegrasIa / classificadosPorAmbos
        throw new UnsupportedOperationException("Incremento 5 ainda nao implementado");
    }
}
