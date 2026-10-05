package br.com.senac.aula06.visao;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import br.com.senac.aula06.config.VisaoProperties;

/**
 * Google Cloud Vision - API ESPECIALIZADA (nao e um modelo de linguagem).
 *
 * Voce pede "features" do catalogo e recebe JSON estruturado com score:
 *   LABEL_DETECTION  -> labelAnnotations[]  (description, score)
 *   TEXT_DETECTION   -> textAnnotations[0].description (texto completo)
 *   FACE_DETECTION   -> faceAnnotations[] (posicao, emocoes) - NAO usamos: LGPD!
 *
 * Endpoint: POST https://vision.googleapis.com/v1/images:annotate
 * Chave: GOOGLE_VISION_API_KEY (Google Cloud Console > APIs & Services >
 *        ativar "Cloud Vision API" > Credentials > API key). Precisa de projeto
 *        com faturamento ativado (ha cota gratuita mensal de 1.000 unidades).
 *
 * Diferenca didatica: aqui NAO existe prompt. Perguntar algo livre ("descreva
 * para e-commerce") nao e possivel - so o que esta no catalogo de features.
 */
@Component
public class GoogleCloudVision extends HttpJsonBase {

    private static final String ENDPOINT = "https://vision.googleapis.com/v1/images:annotate";

    public GoogleCloudVision(ObjectMapper mapper, VisaoProperties props) {
        super(mapper, props.timeoutSegundos());
    }

    @Override
    public String nome() {
        return "google-vision";
    }

    @Override
    public boolean suportaPromptLivre() {
        return false;
    }

    @Override
    public List<Rotulo> rotulos(Imagem imagem) {
        JsonNode resp = anotar(imagem, "LABEL_DETECTION", 10);
        List<Rotulo> lista = new ArrayList<>();
        for (JsonNode n : resp.path("labelAnnotations")) {
            lista.add(new Rotulo(n.path("description").asText().toLowerCase(), n.path("score").asDouble()));
        }
        return lista;
    }

    @Override
    public String ocr(Imagem imagem) {
        JsonNode resp = anotar(imagem, "TEXT_DETECTION", 1);
        // textAnnotations[0] e o texto completo; os demais sao palavra a palavra
        String texto = resp.path("textAnnotations").path(0).path("description").asText("");
        return texto.isBlank() ? "[SEM TEXTO]" : texto;
    }

    @Override
    public String perguntar(Imagem imagem, String prompt) {
        throw new VisaoException(501, "google-vision e uma API especializada: nao aceita prompt livre. "
                + "Use gemini, claude ou openai para perguntas abertas sobre a imagem.");
    }

    private JsonNode anotar(Imagem imagem, String feature, int maxResults) {
        String apiKey = chave("GOOGLE_VISION_API_KEY");

        ObjectNode corpo = mapper.createObjectNode();
        ObjectNode req = corpo.putArray("requests").addObject();
        req.putObject("image").put("content", imagem.base64());
        ArrayNode features = req.putArray("features");
        features.addObject().put("type", feature).put("maxResults", maxResults);

        HttpRequest.Builder http = HttpRequest.newBuilder()
                .uri(URI.create(ENDPOINT))
                .header("x-goog-api-key", apiKey);

        JsonNode raiz = postJson(http, corpo);
        JsonNode primeira = raiz.path("responses").path(0);
        if (primeira.has("error")) {
            throw new VisaoException(502, "Cloud Vision: " + primeira.path("error").path("message").asText());
        }
        return primeira;
    }
}
