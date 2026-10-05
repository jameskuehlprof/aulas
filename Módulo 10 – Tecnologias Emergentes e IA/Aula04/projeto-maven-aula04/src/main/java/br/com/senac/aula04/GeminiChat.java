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
 * Exemplo de integração Java -> Gemini API (Google), usando o mesmo padrão
 * do GeradorTextoIA.java e do ClaudeChat.java (HttpClient nativo + Jackson).
 *
 * Diferença mais importante para destacar em sala: a Gemini API não usa
 * header de autenticação — a chave vai direto na URL, como query param
 * "?key=...". É a segunda "pegadinha" boa depois do "x-api-key" da Claude:
 * três APIs de IA, três jeitos diferentes de mandar a mesma chave.
 *
 * Endpoint: POST https://generativelanguage.googleapis.com/v1beta/models/{modelo}:generateContent?key={API_KEY}
 *
 * Como conseguir a chave: https://aistudio.google.com/apikey
 * (ver o passo a passo completo em aula04-passo-a-passo-claude-gemini.md)
 *
 * Como configurar no Eclipse: Run > Run Configurations > aba Environment >
 * New > Name: GEMINI_API_KEY, Value: sua chave.
 */
public class GeminiChat {

    // Modelo de uso geral, bom equilíbrio custo/qualidade para demonstração.
    // Para o menor custo possível, troque para "gemini-3.5-flash-lite".
    // Nunca invente um nome de modelo: confira a lista atual em
    // https://ai.google.dev/gemini-api/docs/models antes de trocar.
    private static final String MODEL = "gemini-3.8-flash";

    // Quantas vezes tenta de novo quando o Gemini responde 503 (modelo
    // sobrecarregado no lado do Google) antes de desistir.
    private static final int MAX_TENTATIVAS = 3;

    public static void main(String[] args) {
        String pergunta = "Explique em até 3 frases o que é uma API REST, "
                + "como se estivesse explicando para quem está aprendendo Java agora.";

        String apiKey = System.getenv("GEMINI_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            System.out.println("Erro: variável de ambiente GEMINI_API_KEY não encontrada.");
            System.out.println("Configure em Run > Run Configurations > Environment no Eclipse.");
            return;
        }

        try {
            String resposta = perguntarGemini(pergunta, apiKey);
            System.out.println("Pergunta: " + pergunta);
            System.out.println("Resposta do Gemini: " + resposta);
        } catch (IOException | InterruptedException e) {
            System.out.println("Erro ao chamar a API do Gemini: " + e.getMessage());
        }
    }

    private static String perguntarGemini(String pergunta, String apiKey)
            throws IOException, InterruptedException {

        ObjectMapper mapper = new ObjectMapper();

        ObjectNode corpo = mapper.createObjectNode();
        ArrayNode contents = corpo.putArray("contents");
        ObjectNode conteudo = contents.addObject();
        conteudo.put("role", "user");
        ArrayNode parts = conteudo.putArray("parts");
        parts.addObject().put("text", pergunta);

        String corpoJson = mapper.writeValueAsString(corpo);

        String endpoint = String.format(
                "https://generativelanguage.googleapis.com/v1beta/models/%s:generateContent?key=%s",
                MODEL, apiKey);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .timeout(Duration.ofSeconds(30))
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(corpoJson))
                .build();

        HttpResponse<String> response = enviarComRetry(client, request);

        return tratarResposta(response, mapper);
    }

    /**
     * Envia a requisição com duas camadas de resiliência:
     *  - timeout de rede: tenta de novo na hora (pode ser só uma lentidão pontual);
     *  - status 503 (modelo sobrecarregado): espera um pouco mais a cada
     *    tentativa (2s, depois 4s...) antes de tentar de novo — é o padrão
     *    chamado de "backoff exponencial", comum em qualquer API de IA.
     */
    private static HttpResponse<String> enviarComRetry(HttpClient client, HttpRequest request)
            throws IOException, InterruptedException {

        HttpResponse<String> response = null;

        for (int tentativa = 1; tentativa <= MAX_TENTATIVAS; tentativa++) {
            try {
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (HttpTimeoutException e) {
                if (tentativa == MAX_TENTATIVAS) {
                    throw e;
                }
                System.out.println("Timeout na tentativa " + tentativa + ", tentando novamente...");
                continue;
            }

            if (response.statusCode() != 503) {
                return response; // sucesso, ou erro diferente de "sobrecarregado" — tratarResposta decide
            }
            if (tentativa == MAX_TENTATIVAS) {
                return response; // esgotou as tentativas, devolve o 503 para tratarResposta
            }

            long esperaMs = (long) Math.pow(2, tentativa) * 1000; // 2s, 4s, 8s...
            System.out.println("Gemini respondeu 503 (sobrecarregado) na tentativa " + tentativa
                    + " de " + MAX_TENTATIVAS + " — aguardando " + (esperaMs / 1000)
                    + "s antes de tentar de novo...");
            Thread.sleep(esperaMs);
        }

        return response;
    }

    private static String tratarResposta(HttpResponse<String> response, ObjectMapper mapper)
            throws IOException {

        int status = response.statusCode();
        String corpo = response.body();

        if (status != 200) {
            switch (status) {
                case 400:
                    System.out.println("Requisição inválida (400) — confira o formato do JSON "
                            + "enviado. Corpo da resposta:");
                    break;
                case 403:
                    System.out.println("Chave de API inválida, sem permissão, ou o faturamento "
                            + "não foi ativado no projeto (403). Corpo da resposta:");
                    break;
                case 429:
                    System.out.println("Cota gratuita excedida (429). O plano gratuito do "
                            + "Gemini tem limite de requisições por minuto/dia. Corpo da resposta:");
                    break;
                case 503:
                    System.out.println("Gemini continua indisponível (503) após " + MAX_TENTATIVAS
                            + " tentativas. O modelo está sobrecarregado no lado do Google — não é "
                            + "um erro de configuração. Tente de novo em alguns instantes, ou troque "
                            + "MODEL para \"gemini-3.5-flash-lite\" (costuma ter menos fila).");
                    break;
                default:
                    System.out.println("Erro inesperado (" + status + "). Corpo da resposta:");
            }
            System.out.println(corpo);
            return "[erro: status " + status + "]";
        }

        JsonNode raiz = mapper.readTree(corpo);
        JsonNode candidates = raiz.get("candidates");

        // Ponto pedagógico parecido com o da ConsultaCepHttp: aqui o
        // problema pode vir com status 200 e mesmo assim sem resposta —
        // por exemplo quando o filtro de segurança do Gemini bloqueia o
        // conteúdo (finishReason = "SAFETY") em vez de gerar texto.
        if (candidates == null || candidates.isEmpty()) {
            System.out.println("A resposta veio vazia (sem \"candidates\"). Corpo completo:");
            System.out.println(corpo);
            return "[sem resposta — possível bloqueio de segurança]";
        }

        JsonNode primeiroCandidato = candidates.get(0);
        String motivoTermino = primeiroCandidato.path("finishReason").asText("");
        if ("SAFETY".equals(motivoTermino)) {
            System.out.println("O Gemini bloqueou a resposta por segurança (finishReason=SAFETY).");
            return "[resposta bloqueada pelo filtro de segurança]";
        }

        return primeiroCandidato.get("content").get("parts").get(0).get("text").asText();
    }
}
