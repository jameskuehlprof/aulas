package br.com.senac.aula06.visao;

import java.util.List;

/**
 * Contrato comum de todos os provedores de visao computacional.
 *
 * Existem DOIS tipos de servico no mercado - e a aula mostra os dois:
 *
 *  1) APIs especializadas (Google Cloud Vision, Azure AI Vision): voce pede
 *     "features" prontas (LABEL_DETECTION, TEXT_DETECTION, FACE_DETECTION) e
 *     recebe JSON estruturado com confianca. Rapidas, baratas, previsiveis,
 *     mas so fazem o que esta no catalogo.
 *
 *  2) Modelos multimodais (Gemini, Claude, GPT-4o): voce manda a imagem + um
 *     PROMPT em linguagem natural e pede qualquer coisa ("descreva para um
 *     e-commerce", "extraia o total do recibo em JSON"). Flexiveis, mas
 *     precisam de prompt bem escrito e validacao da saida.
 */
public interface VisaoProvider {

    /** "gemini", "claude", "openai" ou "google-vision". */
    String nome();

    /** Ate 10 rotulos do que aparece na imagem, ordenados por confianca. */
    List<Rotulo> rotulos(Imagem imagem);

    /** Texto contido na imagem (OCR), preservando quebras de linha. */
    String ocr(Imagem imagem);

    /**
     * Pergunta livre sobre a imagem (so modelos multimodais). Provedores
     * especializados lancam VisaoException 501 (nao implementado).
     */
    String perguntar(Imagem imagem, String prompt);

    default boolean suportaPromptLivre() {
        return true;
    }
}
