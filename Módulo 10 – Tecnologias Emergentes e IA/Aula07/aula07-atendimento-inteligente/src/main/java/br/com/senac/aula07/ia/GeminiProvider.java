package br.com.senac.aula07.ia;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;

import org.springframework.stereotype.Component;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;

import br.com.senac.aula07.config.IaProperties;

/**
 * Google Gemini - POST https://generativelanguage.googleapis.com/v1beta/models/{modelo}:generateContent
 *
 * Particularidades:
 *   - a chave vai no header "x-goog-api-key" (tambem aceita "?key=" na URL,
 *     como fizemos na Aula 04 - o header e mais seguro porque nao aparece em logs)
 *   - historico vai em "contents", cada item com role "user" ou "model" (nao "assistant")
 *   - cada mensagem tem "parts":[{"text":...}]
 *   - system prompt vai em "system_instruction"
 *   - limite de saida em generationConfig.maxOutputTokens
 *   - o texto volta em candidates[0].content.parts[0].text
 *
 * Chave: variavel de ambiente GEMINI_API_KEY (aistudio.google.com/apikey).
 */
@Component
public class GeminiProvider extends ProviderBase {

    private static final String ENDPOINT =
            "https://generativelanguage.googleapis.com/v1beta/models/%s:generateContent";

    private final IaProperties props;

    public GeminiProvider(ObjectMapper mapper, IaProperties props) {
        super(mapper, props.timeoutSegundos());
        this.props = props;
    }

    @Override
    public String nome() {
        return "gemini";
    }

    @Override
    public RespostaIA conversar(List<MensagemChat> historico, String systemPrompt) {
        String apiKey = chave("GEMINI_API_KEY");
        String modelo = props.gemini().modelo();

        ObjectNode corpo = mapper.createObjectNode();
        if (systemPrompt != null && !systemPrompt.isBlank()) {
            corpo.putObject("system_instruction")
                 .putArray("parts").addObject().put("text", systemPrompt);
        }
        ArrayNode contents = corpo.putArray("contents");
        for (MensagemChat m : historico) {
            ObjectNode no = contents.addObject();
            no.put("role", m.papel() == MensagemChat.Papel.USUARIO ? "user" : "model");
            no.putArray("parts").addObject().put("text", m.conteudo());
        }
        corpo.putObject("generationConfig").put("maxOutputTokens", props.maxTokens());

        HttpRequest.Builder req = HttpRequest.newBuilder()
                .uri(URI.create(String.format(ENDPOINT, modelo)))
                .header("x-goog-api-key", apiKey);

        long inicio = System.currentTimeMillis();
        JsonNode raiz = postJson(req, corpo);
        long tempo = System.currentTimeMillis() - inicio;

        String texto = raiz.path("candidates").path(0).path("content")
                .path("parts").path(0).path("text").asString("");
        JsonNode usage = raiz.get("usageMetadata");

        return new RespostaIA(nome(), modelo, texto,
                intOuZero(usage, "promptTokenCount"),
                intOuZero(usage, "candidatesTokenCount"),
                tempo);
    }
}
