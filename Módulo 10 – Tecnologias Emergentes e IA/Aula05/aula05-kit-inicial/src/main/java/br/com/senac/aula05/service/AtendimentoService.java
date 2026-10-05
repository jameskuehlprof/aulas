package br.com.senac.aula05.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import br.com.senac.aula05.config.IaProperties;
import br.com.senac.aula05.dto.Dtos.AtendimentoResponse;
import br.com.senac.aula05.ia.IaProviderFactory;
import br.com.senac.aula05.ia.MensagemChat;
import br.com.senac.aula05.ia.RespostaIA;

/**
 * DESAFIO - Assistente de atendimento com personalidade e memoria.
 *
 * 1) A "personalidade" e o SYSTEM PROMPT: nome, tom de voz, area de atuacao
 *    da empresa e a regra de recusar educadamente o que esta fora do escopo.
 * 2) A "memoria" e o historico por sessao, guardado em um Map em memoria
 *    (na Aula 07 isso vai para o banco de dados com JPA).
 * 3) O historico e limitado (ia.historico.tamanho) para controlar custo:
 *    cada mensagem antiga reenviada custa tokens de entrada de novo.
 */
@Service
public class AtendimentoService {

    /** Empresa ficticia usada em sala. Os grupos devem trocar pela deles. */
    static final String SYSTEM_PROMPT = """
            Voce e a "Lia", assistente virtual da TechFix, uma assistencia tecnica
            de celulares e notebooks em Blumenau/SC.
            Tom: simpatica, objetiva, sem girias, respostas de no maximo 4 frases.
            Voce SO responde sobre: servicos da TechFix (troca de tela, bateria,
            formatacao, orcamento, prazos, horario de funcionamento 8h-18h de segunda
            a sexta, formas de pagamento: Pix, cartao e dinheiro) e duvidas basicas de
            uso de celular/notebook.
            Se a pergunta for de outro assunto (politica, receitas, programacao, etc.),
            responda exatamente: "Desculpe, so consigo ajudar com assuntos da TechFix
            e cuidados com seu aparelho. Posso ajudar com algo nesse sentido?"
            Nunca invente precos: diga que o orcamento e gratuito e feito na loja.
            """;

    private final IaProviderFactory fabrica;
    private final int tamanhoHistorico;

    /** sessaoId -> historico da conversa (ultimas N mensagens). */
    private final Map<String, List<MensagemChat>> sessoes = new ConcurrentHashMap<>();

    public AtendimentoService(IaProviderFactory fabrica, IaProperties props) {
        this.fabrica = fabrica;
        this.tamanhoHistorico = props.historico().tamanho();
    }

    public AtendimentoResponse atender(String sessaoId, String provedor, String mensagem) {
        List<MensagemChat> historico = sessoes.computeIfAbsent(sessaoId, k -> new ArrayList<>());

        synchronized (historico) {
            historico.add(MensagemChat.usuario(mensagem));
            limitar(historico);

            RespostaIA resposta = fabrica.obter(provedor)
                    .conversar(List.copyOf(historico), SYSTEM_PROMPT);

            historico.add(MensagemChat.assistente(resposta.texto()));
            limitar(historico);

            return new AtendimentoResponse(sessaoId, resposta.texto(), historico.size(), resposta);
        }
    }

    public List<MensagemChat> historico(String sessaoId) {
        return List.copyOf(sessoes.getOrDefault(sessaoId, List.of()));
    }

    public void encerrar(String sessaoId) {
        sessoes.remove(sessaoId);
    }

    /**
     * Mantem so as ultimas N mensagens, sempre comecando por uma mensagem do
     * usuario (o Claude exige que a primeira mensagem seja "user").
     */
    private void limitar(List<MensagemChat> historico) {
        while (historico.size() > tamanhoHistorico
                || (!historico.isEmpty() && historico.get(0).papel() != MensagemChat.Papel.USUARIO)) {
            historico.remove(0);
        }
    }
}
