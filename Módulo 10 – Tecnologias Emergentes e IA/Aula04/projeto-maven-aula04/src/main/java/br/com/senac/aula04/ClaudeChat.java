package br.com.senac.aula04;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Exemplo de integração Java -> Claude API (Anthropic), usando o mesmo
 * padrão do GeradorTextoIA.java (HttpClient nativo + Jackson), só trocando
 * a API de IA usada.
 *
 * Endpoint: POST https://api.anthropic.com/v1/messages
 * Autenticação: header "x-api-key" (NÃO é "Authorization: Bearer" como na
 * maioria das outras APIs de IA — é a primeira "pegadinha" boa de mostrar
 * em sala, já que os alunos acabaram de ver o padrão Bearer em
 * GeradorTextoIA.java).
 *
 * Como conseguir a chave: https://console.anthropic.com/settings/keys
 * (ver o passo a passo completo em aula04-passo-a-passo-claude-gemini.md)
 *
 * Como configurar no Eclipse: Run > Run Configurations > aba Environment >
 * New > Name: ANTHROPIC_API_KEY, Value: sua chave.
 */
public class ClaudeChat {

    private static final String ENDPOINT = "https://api.anthropic.com/v1/messages";

    // Modelo rápido e barato — ótimo para demonstração em sala.
    // Alternativas mais "raciocinadoras" (e mais caras): claude-sonnet-4-6,
    // claude-opus-4-8. Nunca invente um nome de modelo: confira a lista
    // atual em https://platform.claude.com antes de trocar.
    private static final String MODEL = "claude-haiku-4-5";

    private static final int MAX_TOKENS = 1024;

    public static void main(String[] args) {
        String pergunta = "Explique em até 3 frases o que é uma API REST, "
                + "como se estivesse explicando para quem está aprendendo Java agora.";

        String apiKey = System.getenv("ANTHROPIC_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            System.out.println("Erro: variável de ambiente ANTHROPIC_API_KEY não encontrada.");
            System.out.println("Configure em Run > Run Configurations > Environment no Eclipse.");
            return;
        }

        try {
            String resposta = perguntarClaude(pergunta, apiKey);
            System.out.println("Pergunta: " + pergunta);
            System.out.println("Resposta da Claude: " + resposta);
        } catch (IOException | InterruptedException e) {
            System.out.println("Erro ao chamar a API da Claude: " + e.getMessage());
        }
    }

    private static String perguntarClaude(String pergunta, String apiKey)
            throws IOException, InterruptedException {

        ObjectMapper mapper = new ObjectMapper();

        ObjectNode corpo = mapper.createObjectNode();
        corpo.put("model", MODEL);
        corpo.put("max_tokens", MAX_TOKENS);

        ArrayNode mensagens = corpo.putArray("messages");
        ObjectNode mensagemUsuario = mensagens.addObject();
        mensagemUsuario.put("role", "user");
        mensagemUsuario.put("content", pergunta);

        String corpoJson = mapper.writeValueAsString(corpo);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ENDPOINT))
                .timeout(Duration.ofSeconds(30))
                .header("x-api-key", apiKey)
                .header("anthropic-version", "2023-06-01")
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(corpoJson))
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (HttpTimeoutException e) {
            // Mesma estratégia de retry único do GeradorTextoIA.java: às
            // vezes é só uma lentidão pontual da rede do laboratório.
            System.out.println("Timeout na primeira tentativa, tentando novamente...");
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }

        return tratarResposta(response, mapper);
    }

    private static String tratarResposta(HttpResponse<String> response, ObjectMapper mapper)
            throws IOException {

        int status = response.statusCode();
        String corpo = response.body();

        switch (status) {
            case 200:
                JsonNode raiz = mapper.readTree(corpo);
                // O texto vem em content[0].text (content é uma lista de
                // blocos — pode ter mais de um bloco se usar "tools", mas
                // numa pergunta simples como essa é sempre um só).
                return raiz.get("content").get(0).get("text").asText();

            case 400:
                System.out.println("Requisição inválida (400). Corpo da resposta:");
                System.out.println(corpo);
                return "[erro: requisição inválida]";

            case 401:
                System.out.println("Chave de API inválida ou ausente (401). Confira o valor "
                        + "de ANTHROPIC_API_KEY na Run Configuration do Eclipse.");
                return "[erro: chave inválida]";

            case 429:
                // Assim como no GeradorTextoIA.java: 429 pode ser rate limit
                // OU falta de crédito. O campo "type" dentro do JSON de erro
                // diferencia os dois casos.
                System.out.println("Limite atingido (429). Corpo da resposta (verifique o "
                        + "campo \"type\" do erro):");
                System.out.println(corpo);
                return "[erro: limite atingido]";

            default:
                System.out.println("Erro inesperado (" + status + "). Corpo da resposta:");
                System.out.println(corpo);
                return "[erro: status " + status + "]";
        }
    }
}
