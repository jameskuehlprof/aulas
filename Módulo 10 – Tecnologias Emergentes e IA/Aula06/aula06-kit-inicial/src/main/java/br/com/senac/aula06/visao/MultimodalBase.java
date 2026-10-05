package br.com.senac.aula06.visao;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Base dos modelos multimodais (Gemini, Claude, GPT): rotulos e OCR sao
 * apenas PROMPTS bem escritos + parse do JSON devolvido. Cada subclasse so
 * precisa implementar perguntar() com o formato HTTP do seu provedor.
 */
public abstract class MultimodalBase extends HttpJsonBase {

    protected static final String PROMPT_ROTULOS = """
            Liste ate 10 rotulos (objetos, cenas, conceitos) presentes na imagem.
            Responda SOMENTE com um JSON no formato:
            [{"nome":"cachorro","confianca":0.97}, ...]
            - nome em portugues do Brasil, minusculo, sem acento
            - confianca entre 0 e 1
            - ordenado do mais confiante para o menos
            Nao escreva nada alem do JSON.
            """;

    protected static final String PROMPT_OCR = """
            Transcreva TODO o texto presente na imagem, exatamente como esta escrito,
            preservando as quebras de linha. Nao traduza, nao corrija, nao comente.
            Se nao houver texto legivel, responda apenas: [SEM TEXTO]
            """;

    protected MultimodalBase(ObjectMapper mapper, int timeoutSegundos) {
        super(mapper, timeoutSegundos);
    }

    @Override
    public List<Rotulo> rotulos(Imagem imagem) {
        String resposta = perguntar(imagem, PROMPT_ROTULOS);
        try {
            JsonNode arr = mapper.readTree(limparJson(resposta));
            List<Rotulo> lista = new ArrayList<>();
            for (JsonNode n : arr) {
                lista.add(new Rotulo(n.path("nome").asText("?"), n.path("confianca").asDouble(0)));
            }
            lista.sort(Comparator.comparingDouble(Rotulo::confianca).reversed());
            return lista;
        } catch (Exception e) {
            log.warn("[{}] resposta de rotulos nao veio em JSON: {}", nome(), resposta);
            throw new VisaoException(502, "O modelo " + nome() + " nao devolveu JSON valido para os rotulos.");
        }
    }

    @Override
    public String ocr(Imagem imagem) {
        return perguntar(imagem, PROMPT_OCR).trim();
    }
}
