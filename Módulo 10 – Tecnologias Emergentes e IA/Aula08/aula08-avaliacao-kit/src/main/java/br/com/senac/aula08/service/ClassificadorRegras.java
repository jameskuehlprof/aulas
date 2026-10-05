package br.com.senac.aula08.service;

import java.text.Normalizer;
import java.util.List;

import org.springframework.stereotype.Component;

import br.com.senac.aula08.model.Ticket.Prioridade;

/**
 * INCREMENTO 3 (Aula 02 - Machine Learning): classificador "classico" por regras.
 *
 * E o baseline que sera comparado com a classificacao da IA generativa.
 * Implemente:
 *   - categoria(texto): "orcamento" | "prazo" | "garantia" | "suporte" | "outros",
 *     escolhendo a categoria com MAIS palavras-chave encontradas no texto normalizado.
 *   - prioridade(texto): ALTA se houver palavras de urgencia ("urgente", "hoje",
 *     "nao liga", "trabalho"...), MEDIA para ("amanha", "lento", "travando"...), senao BAIXA.
 */
@Component
public class ClassificadorRegras {

    public static final List<String> CATEGORIAS = List.of("orcamento", "prazo", "garantia", "suporte", "outros");

    public String categoria(String texto) {
        // TODO Incremento 3: mapa categoria -> palavras-chave; contar ocorrencias; devolver a melhor
        return "outros";
    }

    public Prioridade prioridade(String texto) {
        // TODO Incremento 3
        return Prioridade.BAIXA;
    }

    /** Pronto: "  Vocês TROCAM tela?? " -> "voces trocam tela" */
    static String normalizar(String texto) {
        String semAcento = Normalizer.normalize(texto == null ? "" : texto, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        return semAcento.toLowerCase().replaceAll("[^\\p{L}\\p{N}\\s]", " ").replaceAll("\\s+", " ").trim();
    }
}
