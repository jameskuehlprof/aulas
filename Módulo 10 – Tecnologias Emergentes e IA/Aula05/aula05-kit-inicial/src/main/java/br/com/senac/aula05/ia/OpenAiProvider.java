package br.com.senac.aula05.ia;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import br.com.senac.aula05.config.IaProperties;

/**
 * OpenAI ChatGPT - POST https://api.openai.com/v1/chat/completions
 *
 * Particularidades:
 *   - autenticacao "Authorization: Bearer <chave>" (o padrao mais comum)
 *   - system prompt e uma mensagem normal com role "system"
 *   - limite de saida no campo "max_tokens"
 *   - o texto volta em choices[0].message.content
 *
 * Chave: variavel de ambiente OPENAI_API_KEY (platform.openai.com).
 */
@Component
public class OpenAiProvider extends ProviderBase {

    private static final String ENDPOINT = "https://api.openai.com/v1/chat/completions";

    private final IaProperties props;

    public OpenAiProvider(ObjectMapper mapper, IaProperties props) {
        super(mapper, props.timeoutSegundos());
        this.props = props;
    }

    @Override
    public String nome() {
        return "openai";
    }

    @Override
    public RespostaIA conversar(List<MensagemChat> historico, String systemPrompt) {
        String apiKey = chave("OPENAI_API_KEY");
        String modelo = props.openai().modelo();

        ObjectNode corpo = mapper.createObjectNode();
        corpo.put("model", modelo);
        corpo.put("max_tokens", props.maxTokens());
        ArrayNode messages = corpo.putArray("messages");
        if (systemPrompt != null && !systemPrompt.isBlank()) {
            ObjectNode sys = messages.addObject();
            sys.put("role", "system");
            sys.put("content", systemPrompt);
        }
        for (MensagemChat m : historico) {
            ObjectNode no = messages.addObject();
            no.put("role", m.papel() == MensagemChat.Papel.USUARIO ? "user" : "assistant");
            no.put("content", m.conteudo());
        }

        HttpRequest.Builder req = HttpRequest.newBuilder()
                .uri(URI.create(ENDPOINT))
                .header("Authorization", "Bearer " + apiKey);

        long inicio = System.currentTimeMillis();
        JsonNode raiz = postJson(req, corpo);
        long tempo = System.currentTimeMillis() - inicio;

        String texto = raiz.path("choices").path(0).path("message").path("content").asText("");
        JsonNode usage = raiz.get("usage");

        return new RespostaIA(nome(), modelo, texto,
                intOuZero(usage, "prompt_tokens"),
                intOuZero(usage, "completion_tokens"),
                tempo);
    }
}
