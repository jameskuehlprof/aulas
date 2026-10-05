package br.com.senac.aula05.dto;

import java.util.List;

import br.com.senac.aula05.ia.RespostaIA;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Todos os DTOs (records) de entrada e saida da API, num unico arquivo para
 * facilitar a leitura em sala. Em projetos reais, um record por arquivo.
 */
public final class Dtos {

    private Dtos() {}

    // ----------------------------------------------------------- Exercicio 1
    public record ChatRequest(
            @Schema(example = "claude", description = "claude | openai | gemini")
            @NotBlank String provedor,
            @Schema(example = "Explique em 2 frases o que e uma API REST.")
            @NotBlank @Size(max = 4000) String mensagem) {}

    // ----------------------------------------------------------- Exercicio 2
    public record ResumoRequest(
            @NotBlank String provedor,
            @Schema(description = "Texto a resumir (cole um paragrafo de noticia)")
            @NotBlank @Size(max = 12000) String texto,
            @Schema(example = "3", description = "Numero maximo de linhas do resumo")
            @Min(1) @Max(10) int linhas) {}

    // ----------------------------------------------------------- Exercicio 3
    public record EmailCobrancaRequest(
            @NotBlank String provedor,
            @Schema(example = "Maria Silva") @NotBlank String nomeCliente,
            @Schema(example = "349.90") @NotBlank String valor,
            @Schema(example = "25/09/2026") @NotBlank String prazo,
            @Schema(example = "cordial", description = "cordial | firme | descontraido")
            @NotBlank String tom) {}

    // ----------------------------------------------------------- Bonus (Arena)
    public record ArenaRequest(
            @NotBlank @Size(max = 4000) String mensagem) {}

    public record ArenaResponse(String mensagem, List<RespostaIA> respostas, String maisRapido) {}

    // ----------------------------------------------------------- Desafio
    public record AtendimentoRequest(
            @Schema(example = "claude") @NotBlank String provedor,
            @Schema(example = "Quais formas de pagamento voces aceitam?")
            @NotBlank @Size(max = 4000) String mensagem) {}

    public record AtendimentoResponse(
            String sessaoId,
            String resposta,
            int mensagensNoHistorico,
            RespostaIA detalhes) {}
}
