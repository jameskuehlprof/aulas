package br.com.senac.aula05.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import br.com.senac.aula05.dto.Dtos.ArenaResponse;
import br.com.senac.aula05.ia.IaException;
import br.com.senac.aula05.ia.IaProvider;
import br.com.senac.aula05.ia.IaProviderFactory;
import br.com.senac.aula05.ia.RespostaIA;

/**
 * Regras de negocio dos Exercicios 1, 2, 3 e do bonus "Arena".
 *
 * Repare: e AQUI (Service) que o prompt e montado e a IA e chamada.
 * O Controller so recebe o JSON e devolve a resposta - regra que vale para
 * toda a arquitetura em camadas que veremos na Aula 07.
 */
@Service
public class ChatService {

    private final IaProviderFactory fabrica;

    public ChatService(IaProviderFactory fabrica) {
        this.fabrica = fabrica;
    }

    // ----------------------------------------------------------- Exercicio 1
    public RespostaIA chat(String provedor, String mensagem) {
        return fabrica.obter(provedor).perguntar(mensagem);
    }

    // ----------------------------------------------------------- Exercicio 2
    public RespostaIA resumir(String provedor, String texto, int linhas) {
        // Tecnica de prompt: instrucao explicita + delimitador do texto do usuario
        String prompt = """
                Resuma o texto abaixo em no maximo %d linhas, em portugues do Brasil,
                mantendo apenas as informacoes essenciais. Nao adicione comentarios.

                TEXTO:
                \"\"\"
                %s
                \"\"\"
                """.formatted(linhas, texto);
        return fabrica.obter(provedor).perguntar(prompt);
    }

    // ----------------------------------------------------------- Exercicio 3
    public RespostaIA emailCobranca(String provedor, String nome, String valor, String prazo, String tom) {
        String prompt = """
                Escreva um e-mail de cobranca em portugues do Brasil.
                Dados:
                - Nome do cliente: %s
                - Valor em aberto: R$ %s
                - Prazo para pagamento: %s
                Tom desejado: %s.
                Regras: maximo de 120 palavras, incluir assunto na primeira linha
                no formato "Assunto: ...", ser educado e nunca ameacar o cliente.
                """.formatted(nome, valor, prazo, tom);
        return fabrica.obter(provedor).perguntar(prompt);
    }

    // ----------------------------------------------------------- Bonus: Arena
    /**
     * Manda a MESMA pergunta para os tres provedores em paralelo e compara.
     * Se um provedor falhar (sem chave, sem credito...), a arena continua
     * com os outros - o erro vira uma "resposta" com o texto do problema.
     */
    public ArenaResponse arena(String mensagem) {
        List<CompletableFuture<RespostaIA>> futuros = new ArrayList<>();
        for (IaProvider p : fabrica.todos()) {
            futuros.add(CompletableFuture.supplyAsync(() -> {
                try {
                    return p.perguntar(mensagem);
                } catch (IaException e) {
                    return new RespostaIA(p.nome(), "-", "[erro] " + e.getMessage(), 0, 0, -1);
                }
            }));
        }
        List<RespostaIA> respostas = futuros.stream().map(CompletableFuture::join).toList();

        String maisRapido = respostas.stream()
                .filter(r -> r.tempoMs() >= 0)
                .min(Comparator.comparingLong(RespostaIA::tempoMs))
                .map(RespostaIA::provedor)
                .orElse("nenhum (todos falharam)");

        return new ArenaResponse(mensagem, respostas, maisRapido);
    }
}
