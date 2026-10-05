package br.com.senac.aula04;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.Scanner;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Aula 04 - "Projeto da aula" / Desafio: Gerador automático de texto resiliente.
 *
 * Implementação de referência (gabarito) para o Desafio do docx de exercícios:
 *  - Lê o prompt do usuário via terminal
 *  - Monta e envia a requisição HTTP POST com o prompt em JSON
 *  - Exibe a resposta formatada
 *  - Trata erro 401 (key inválida), 429 (limite excedido) e timeout,
 *    cada um com mensagem específica
 *  - Implementa 1 nova tentativa automática (retry) em caso de timeout
 *
 * Usa a API de Chat da OpenAI como exemplo (o mesmo padrão de autenticação
 * "Authorization: Bearer <chave>" que está no slide 4). Para usar a API do
 * Claude (Anthropic) em vez da OpenAI, troque a URL e o header de autenticação
 * (Anthropic usa o header "x-api-key", não "Authorization: Bearer") - ótimo
 * gancho em sala para mostrar que nem toda API segue o mesmo padrão.
 *
 * ANTES DE RODAR:
 *   1) Configure a variável de ambiente OPENAI_API_KEY com uma chave de teste
 *      (Eclipse: Run > Run Configurations > Environment > New).
 *      NUNCA cole a chave direto no código.
 *   2) Dependência Maven (pom.xml):
 *        <dependency>
 *          <groupId>com.fasterxml.jackson.core</groupId>
 *          <artifactId>jackson-databind</artifactId>
 *          <version>2.17.0</version>
 *        </dependency>
 */
public class GeradorTextoIA {

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String MODELO = "gpt-4o-mini";

    public static void main(String[] args) {
    	
        String apiKey = System.getenv("OPENAI_API_KEY");
        System.out.println("Chave lida (tamanho): " + (apiKey == null ? "null" : apiKey.length() + " caracteres, começa com " + apiKey.substring(0, Math.min(7, apiKey.length()))));
        if (apiKey == null || apiKey.isBlank()) {
            System.out.println("Erro de configuração: variável de ambiente OPENAI_API_KEY não encontrada.");
            System.out.println("Configure a chave antes de rodar (nunca escreva a chave no código-fonte).");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o prompt para a IA: ");
        String prompt = scanner.nextLine();

        try {
            String respostaGerada = chamarApiComRetry(apiKey, prompt, /* jaTentouRetry = */ false);
            if (respostaGerada != null) {
                System.out.println("\n--- Resposta da IA ---");
                System.out.println(respostaGerada);
            }
        } finally {
            scanner.close();
        }
    }

    /**
     * Monta e envia a requisição. Em caso de timeout, tenta mais UMA vez
     * (retry simples, exigido pelo Desafio) antes de desistir.
     */
    private static String chamarApiComRetry(String apiKey, String prompt, boolean jaTentouRetry) {
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        try {
            String corpoJson = montarCorpoRequisicao(prompt);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .timeout(Duration.ofSeconds(15))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(corpoJson))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return tratarResposta(response);

        } catch (HttpTimeoutException e) {
            if (!jaTentouRetry) {
                System.out.println("Timeout na chamada à API. Tentando novamente (1 nova tentativa)...");
                return chamarApiComRetry(apiKey, prompt, true);
            }
            System.out.println("A API demorou demais para responder, mesmo após uma nova tentativa. Tente de novo mais tarde.");
            return null;

        } catch (IOException e) {
            System.out.println("Não foi possível conectar à API (verifique sua conexão com a internet): " + e.getMessage());
            return null;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Requisição interrompida.");
            return null;
        }
    }

    private static String montarCorpoRequisicao(String prompt) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode raiz = mapper.createObjectNode();
        raiz.put("model", MODELO);

        ObjectNode mensagem = mapper.createObjectNode();
        mensagem.put("role", "user");
        mensagem.put("content", prompt);
        raiz.putArray("messages").add(mensagem);

        return mapper.writeValueAsString(raiz);
    }

    /**
     * Trata cada status HTTP com uma mensagem específica, como pede o Desafio.
     * Nunca mostra stack trace bruto para quem está usando o programa.
     */
    private static String tratarResposta(HttpResponse<String> response) throws IOException {
        int status = response.statusCode();
        ObjectMapper mapper = new ObjectMapper();

        switch (status) {
            case 200:
                JsonNode json = mapper.readTree(response.body());
                return json.get("choices").get(0).get("message").get("content").asText();

            case 401:
                System.out.println("Erro 401: a API Key é inválida ou não foi enviada corretamente. Confira a variável de ambiente.");
                return null;

            case 429:
                System.out.println("Erro 429: limite de uso (rate limit) excedido. Aguarde um pouco antes de tentar de novo.");
                return null;

            case 400:
                System.out.println("Erro 400: a requisição enviada está com formato inválido. Confira o JSON montado.");
                return null;

            default:
                System.out.println("A API respondeu com status " + status + ". Corpo da resposta: " + response.body());
                return null;
        }
    }
}
