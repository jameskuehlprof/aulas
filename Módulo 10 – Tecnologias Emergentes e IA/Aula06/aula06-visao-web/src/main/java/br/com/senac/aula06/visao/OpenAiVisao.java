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
 * ChatGPT (OpenAI) - modelo multimodal (gpt-4o-mini enxerga imagens).
 *
 * A imagem vai como "image_url" com uma data URL (data:image/jpeg;base64,...).
 *
 * {"model":"gpt-4o-mini","max_tokens":800,"messages":[{"role":"user","content":[
 *     {"type":"text","text":"Liste os rotulos..."},
 *     {"type":"image_url","image_url":{"url":"data:image/jpeg;base64,<base64>"}}
 * ]}]}
 */
@Component
public class OpenAiVisao extends MultimodalBase {

    private static final String ENDPOINT = "https://api.openai.com/v1/chat/completions";

    private final VisaoProperties props;

    public OpenAiVisao(ObjectMapper mapper, VisaoProperties props) {
        super(mapper, props.timeoutSegundos());
        this.props = props;
    }

    @Override
    public String nome() {
        return "openai";
    }

    @Override
    public String perguntar(Imagem imagem, String prompt) {
        String apiKey = chave("OPENAI_API_KEY");

        ObjectNode corpo = mapper.createObjectNode();
        corpo.put("model", props.openai().modelo());
        corpo.put("max_tokens", props.maxTokens());
        ObjectNode msg = corpo.putArray("messages").addObject().put("role", "user");
        ArrayNode content = msg.putArray("content");
        content.addObject().put("type", "text").put("text", prompt);
        content.addObject().put("type", "image_url").putObject("image_url").put("url", imagem.dataUrl());

        HttpRequest.Builder req = HttpRequest.newBuilder()
                .uri(URI.create(ENDPOINT))
                .header("Authorization", "Bearer " + apiKey);

        JsonNode raiz = postJson(req, corpo);
        return raiz.path("choices").path(0).path("message").path("content").asText("");
    }
}
