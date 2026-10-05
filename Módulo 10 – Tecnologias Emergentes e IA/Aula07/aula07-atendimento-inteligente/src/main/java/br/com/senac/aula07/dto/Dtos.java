package br.com.senac.aula07.dto;

import java.time.LocalDateTime;

import br.com.senac.aula07.model.Pergunta;
import br.com.senac.aula07.model.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** DTOs (records) de entrada e saida. Entidades JPA nunca saem direto no JSON. */
public final class Dtos {

    private Dtos() {}

    // ------------------------------------------------------------ Usuario
    public record UsuarioRequest(
            @Schema(example = "Maria Silva") @NotBlank @Size(max = 120) String nome,
            @Schema(example = "maria@email.com") @NotBlank @Email @Size(max = 160) String email) {}

    public record UsuarioResponse(Long id, String nome, String email, LocalDateTime criadoEm) {
        public static UsuarioResponse de(Usuario u) {
            return new UsuarioResponse(u.getId(), u.getNome(), u.getEmail(), u.getCriadoEm());
        }
    }

    // ------------------------------------------------------------ Pergunta
    public record PerguntaRequest(
            @Schema(example = "1") @NotNull Long usuarioId,
            @Schema(example = "Voces trocam a tela do iPhone 13? Quanto tempo demora?")
            @NotBlank @Size(max = 4000) String texto) {}

    public record PerguntaResponse(
            Long id,
            Long usuarioId,
            String texto,
            String resposta,
            Pergunta.Origem origem,
            String provedor,
            String modelo,
            int tokensEntrada,
            int tokensSaida,
            long tempoMs,
            String categoria,
            LocalDateTime dataHora) {

        public static PerguntaResponse de(Pergunta p) {
            return new PerguntaResponse(p.getId(), p.getUsuario().getId(), p.getTexto(), p.getResposta(),
                    p.getOrigem(), p.getProvedor(), p.getModelo(), p.getTokensEntrada(), p.getTokensSaida(),
                    p.getTempoMs(), p.getCategoria(), p.getDataHora());
        }
    }

    // ------------------------------------------------------------ Metricas (Desafio)
    public record MetricasResponse(
            long totalPerguntas,
            long respondidasPelaIa,
            long respondidasPeloCache,
            long fallbacks,
            double taxaCacheHitPercentual,
            double tempoMedioIaMs,
            double tempoMedioCacheMs,
            long totalTokens,
            boolean cacheAtivo) {}
}
