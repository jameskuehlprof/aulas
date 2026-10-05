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
 * Exemplo prático de "function calling" (chamada de função): a Gemini API
 * decide, sozinha, chamar um aplicativo público de verdade — a API ViaCEP,
 * a mesma já usada em ConsultaCepHttp.java — para responder a uma pergunta
 * em linguagem natural.
 *
 * Diferença para GeminiChat.java: lá, a gente manda uma pergunta e a IA
 * responde só com o que ela já "sabe" (texto puro). Aqui, a gente ensina a
 * IA que existe uma FERRAMENTA chamada "consultar_cep" que ela pode pedir
 * para o programa Java executar sempre que precisar saber o endereço de um
 * CEP — algo que a IA sozinha não sabe (ela não tem acesso à internet).
 *
 * O fluxo tem 3 idas e vindas:
 *
 *   1. Java pergunta para a Gemini em linguagem natural + avisa que existe
 *      a ferramenta "consultar_cep" disponível.
 *   2. Gemini NÃO responde com texto — responde pedindo para o Java
 *      executar consultar_cep(cep = "01310930"). É um "functionCall".
 *   3. Java executa de verdade a chamada HTTP para viacep.com.br (o
 *      "aplicativo público" sendo chamado) e manda o resultado de volta
 *      para a Gemini.
 *   4. Só na segunda resposta a Gemini gera o texto final, já usando o
 *      endereço real que o ViaCEP devolveu.
 *
 * Isso é o mesmo princípio por trás de assistentes de IA que "pesquisam na
 * web" ou "consultam sua agenda" — o modelo não faz a chamada sozinho, ele
 * PEDE para a aplicação fazer e devolver o resultado.
 */
public class GeminiConsultaCep {

    private static final String MODEL = "gemini-3.8-flash";
    private static final String NOME_FUNCAO = "consultar_cep";

    // Quantas vezes tenta de novo quando o Gemini responde 503 (modelo
    // sobrecarregado no lado do Google) antes de desistir.
    private static final int MAX_TENTATIVAS = 3;

    public static void main(String[] args) {
        String pergunta = "Estou de mudança para o endereço do CEP 01310-930. "
                + "Me diga em que bairro e cidade fica, e dê uma dica rápida "
                + "de boas-vindas para quem está chegando nessa região.";

        String apiKey = System.getenv("GEMINI_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            System.out.println("Erro: variável de ambiente GEMINI_API_KEY não encontrada.");
            System.out.println("Configure em Run > Run Configurations > Environment no Eclipse.");
            return;
        }

        try {
            String resposta = perguntarComFuncao(pergunta, apiKey);
            System.out.println();
            System.out.println("Pergunta: " + pergunta);
            System.out.println("Resposta final da Gemini: " + resposta);
        } catch (IOException | InterruptedException e) {
            System.out.println("Erro no fluxo Gemini + ViaCEP: " + e.getMessage());
        }
    }

    private static String perguntarComFuncao(String pergunta, String apiKey)
            throws IOException, InterruptedException {

        ObjectMapper mapper = new ObjectMapper();

        // ---------- 1ª chamada: pergunta + ferramenta disponível ----------
        ArrayNode historico = mapper.createArrayNode();
        ObjectNode mensagemUsuario = historico.addObject();
        mensagemUsuario.put("role", "user");
        mensagemUsuario.putArray("parts").addObject().put("text", pergunta);

        ObjectNode corpo1 = mapper.createObjectNode();
        corpo1.set("contents", historico);
        corpo1.set("tools", declararFerramentas(mapper));

        JsonNode resposta1 = chamarGemini(corpo1, apiKey, mapper);
        JsonNode conteudoModelo = resposta1.get("candidates").get(0).get("content");
        JsonNode primeiraParte = conteudoModelo.get("parts").get(0);

        if (!primeiraParte.has("functionCall")) {
            // A Gemini decidiu que não precisava da ferramenta e já
            // respondeu direto em texto — também é um resultado válido.
            System.out.println("[a Gemini respondeu direto, sem usar a ferramenta]");
            return primeiraParte.get("text").asText();
        }

        JsonNode chamada = primeiraParte.get("functionCall");
        String nomeFuncaoPedida = chamada.get("name").asText();
        String cepPedido = chamada.get("args").get("cep").asText();

        System.out.println("[a Gemini pediu para chamar \"" + nomeFuncaoPedida
                + "\" com cep=\"" + cepPedido + "\"]");

        // ---------- Aqui é o "aplicativo público" sendo chamado de verdade ----------
        JsonNode enderecoReal = consultarViaCepDeVerdade(cepPedido, mapper);
        System.out.println("[ViaCEP respondeu: " + enderecoReal + "]");

        // ---------- 2ª chamada: devolve o resultado real para a Gemini ----------
        historico.add(conteudoModelo); // reaproveita o pedido de função no histórico

        ObjectNode mensagemFuncao = historico.addObject();
        mensagemFuncao.put("role", "function");
        ObjectNode functionResponsePart = mensagemFuncao.putArray("parts").addObject();
        ObjectNode functionResponse = functionResponsePart.putObject("functionResponse");
        functionResponse.put("name", nomeFuncaoPedida);
        ObjectNode responseWrapper = functionResponse.putObject("response");
        responseWrapper.put("name", nomeFuncaoPedida);
        responseWrapper.set("content", enderecoReal);

        ObjectNode corpo2 = mapper.createObjectNode();
        corpo2.set("contents", historico);
        corpo2.set("tools", declararFerramentas(mapper));

        JsonNode resposta2 = chamarGemini(corpo2, apiKey, mapper);
        return resposta2.get("candidates").get(0).get("content")
                .get("parts").get(0).get("text").asText();
    }

    /**
     * Descreve para a Gemini que existe uma função chamada "consultar_cep",
     * o que ela faz e quais parâmetros espera — no formato OpenAPI, que é
     * o mesmo formato usado por praticamente todas as APIs de IA com
     * function calling (Claude e OpenAI usam uma ideia muito parecida).
     */
    private static ArrayNode declararFerramentas(ObjectMapper mapper) {
        ArrayNode tools = mapper.createArrayNode();
        ObjectNode tool = tools.addObject();
        ArrayNode functionDeclarations = tool.putArray("functionDeclarations");

        ObjectNode funcao = functionDeclarations.addObject();
        funcao.put("name", NOME_FUNCAO);
        funcao.put("description",
                "Consulta o endereço (rua, bairro, cidade e estado) de um CEP "
                        + "brasileiro usando a base pública de Correios via ViaCEP.");

        ObjectNode parametros = funcao.putObject("parameters");
        parametros.put("type", "object");
        ObjectNode properties = parametros.putObject("properties");
        ObjectNode cepProperty = properties.putObject("cep");
        cepProperty.put("type", "string");
        cepProperty.put("description", "CEP brasileiro, só números ou com hífen, ex.: 01310930 ou 01310-930.");
        parametros.putArray("required").add("cep");

        return tools;
    }

    /** Mesma chamada HTTP usada em GeminiChat.java, isolada para reaproveitar nas duas idas. */
    private static JsonNode chamarGemini(ObjectNode corpo, String apiKey, ObjectMapper mapper)
            throws IOException, InterruptedException {

        String endpoint = String.format(
                "https://generativelanguage.googleapis.com/v1beta/models/%s:generateContent?key=%s",
                MODEL, apiKey);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .timeout(Duration.ofSeconds(30))
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(corpo)))
                .build();

        HttpResponse<String> response = enviarComRetry(client, request);

        if (response.statusCode() != 200) {
            if (response.statusCode() == 503) {
                System.out.println("Gemini continua indisponível (503) após " + MAX_TENTATIVAS
                        + " tentativas. O modelo está sobrecarregado no lado do Google — não é um "
                        + "erro de configuração. Tente de novo em alguns instantes, ou troque MODEL "
                        + "para \"gemini-3.5-flash-lite\" (costuma ter menos fila).");
            } else {
                System.out.println("Erro (" + response.statusCode() + ") ao chamar a Gemini. Corpo da resposta:");
            }
            System.out.println(response.body());
            throw new IOException("Gemini retornou status " + response.statusCode());
        }

        return mapper.readTree(response.body());
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
                return response; // sucesso, ou erro diferente de "sobrecarregado" — quem chamou decide
            }
            if (tentativa == MAX_TENTATIVAS) {
                return response; // esgotou as tentativas, devolve o 503 para o chamador tratar
            }

            long esperaMs = (long) Math.pow(2, tentativa) * 1000; // 2s, 4s, 8s...
            System.out.println("Gemini respondeu 503 (sobrecarregado) na tentativa " + tentativa
                    + " de " + MAX_TENTATIVAS + " — aguardando " + (esperaMs / 1000)
                    + "s antes de tentar de novo...");
            Thread.sleep(esperaMs);
        }

        return response;
    }

    /**
     * O "aplicativo público" de verdade sendo chamado — mesma API e mesmo
     * cuidado pedagógico de ConsultaCepHttp.java: a ViaCEP responde HTTP
     * 200 mesmo para CEP inexistente, com "erro": true dentro do JSON.
     */
    private static JsonNode consultarViaCepDeVerdade(String cep, ObjectMapper mapper)
            throws IOException, InterruptedException {

        String cepLimpo = cep.replaceAll("\\D", "");
        String endpoint = "https://viacep.com.br/ws/" + cepLimpo + "/json/";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .timeout(Duration.ofSeconds(15))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonNode resultado = mapper.readTree(response.body());

        if (resultado.path("erro").asBoolean(false)) {
            // Igual ao ConsultaCepHttp.java: status 200, mas o CEP não existe.
            // Mandamos esse "erro" de volta para a Gemini também — ela precisa
            // saber que a consulta falhou para poder avisar o usuário direito.
            System.out.println("[CEP não encontrado pela ViaCEP]");
        }

        return resultado;
    }
}
