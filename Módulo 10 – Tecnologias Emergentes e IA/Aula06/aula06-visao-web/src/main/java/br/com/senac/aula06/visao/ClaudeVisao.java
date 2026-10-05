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
 * Claude (Anthropic) - modelo multimodal.
 *
 * O "content" da mensagem do usuario deixa de ser uma String e vira uma
 * LISTA de blocos: um bloco "image" (base64) e um bloco "text" com o prompt.
 *
 * {"model":"claude-haiku-4-5","max_tokens":800,"messages":[{"role":"user","content":[
 *     {"type":"image","source":{"type":"base64","media_type":"image/jpeg","data":"<base64>"}},
 *     {"type":"text","text":"Liste os rotulos..."}
 * ]}]}
 */
@Component
public class ClaudeVisao extends MultimodalBase {

    private static final String ENDPOINT = "https://api.anthropic.com/v1/messages";

    private final VisaoProperties props;

    public ClaudeVisao(ObjectMapper mapper, VisaoProperties props) {
        super(mapper, props.timeoutSegundos());
        this.props = props;
    }

    @Override
    public String nome() {
        return "claude";
    }

    @Override
    public String perguntar(Imagem imagem, String prompt) {
        String apiKey = chave("ANTHROPIC_API_KEY");

        ObjectNode corpo = mapper.createObjectNode();
        corpo.put("model", props.claude().modelo());
        corpo.put("max_tokens", props.maxTokens());
        ObjectNode msg = corpo.putArray("messages").addObject().put("role", "user");
        ArrayNode content = msg.putArray("content");

        ObjectNode img = content.addObject().put("type", "image");
        ObjectNode source = img.putObject("source");
        source.put("type", "base64");
        source.put("media_type", imagem.mimeType());
        source.put("data", imagem.base64());

        content.addObject().put("type", "text").put("text", prompt);

        HttpRequest.Builder req = HttpRequest.newBuilder()
                .uri(URI.create(ENDPOINT))
                .header("x-api-key", apiKey)
                .header("anthropic-version", "2023-06-01");

        JsonNode raiz = postJson(req, corpo);
        return raiz.path("content").path(0).path("text").asText("");
    }
}
