package br.com.senac.aula06.visao;

import java.net.URI;
import java.net.http.HttpRequest;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import br.com.senac.aula06.config.VisaoProperties;

/**
 * Gemini (Google) - modelo multimodal.
 *
 * A imagem vai como uma "part" de inline_data (mime_type + base64) ANTES da
 * part de texto com o prompt. Mesmo endpoint e mesma chave da Aula 05.
 *
 * {"contents":[{"role":"user","parts":[
 *     {"inline_data":{"mime_type":"image/jpeg","data":"<base64>"}},
 *     {"text":"Liste os rotulos..."}
 * ]}]}
 */
@Component
public class GeminiVisao extends MultimodalBase {

    private static final String ENDPOINT =
            "https://generativelanguage.googleapis.com/v1beta/models/%s:generateContent";

    private final VisaoProperties props;

    public GeminiVisao(ObjectMapper mapper, VisaoProperties props) {
        super(mapper, props.timeoutSegundos());
        this.props = props;
    }

    @Override
    public String nome() {
        return "gemini";
    }

    @Override
    public String perguntar(Imagem imagem, String prompt) {
        String apiKey = chave("GEMINI_API_KEY");

        ObjectNode corpo = mapper.createObjectNode();
        ArrayNode parts = corpo.putArray("contents").addObject().put("role", "user").putArray("parts");
        ObjectNode inline = parts.addObject().putObject("inline_data");
        inline.put("mime_type", imagem.mimeType());
        inline.put("data", imagem.base64());
        parts.addObject().put("text", prompt);
        corpo.putObject("generationConfig").put("maxOutputTokens", props.maxTokens());

        HttpRequest.Builder req = HttpRequest.newBuilder()
                .uri(URI.create(String.format(ENDPOINT, props.gemini().modelo())))
                .header("x-goog-api-key", apiKey);

        JsonNode raiz = postJson(req, corpo);
        return raiz.path("candidates").path(0).path("content").path("parts").path(0).path("text").asText("");
    }
}
