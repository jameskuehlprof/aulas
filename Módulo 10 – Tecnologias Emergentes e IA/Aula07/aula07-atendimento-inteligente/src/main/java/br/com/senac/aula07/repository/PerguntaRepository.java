package br.com.senac.aula07.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.senac.aula07.model.Pergunta;

public interface PerguntaRepository extends JpaRepository<Pergunta, Long> {

    /** Historico do usuario, mais recente primeiro (Exercicio 2). */
    List<Pergunta> findByUsuarioIdOrderByDataHoraDesc(Long usuarioId);

    /** Ultimas N perguntas do usuario para montar o contexto da conversa. */
    List<Pergunta> findTop6ByUsuarioIdOrderByDataHoraDesc(Long usuarioId);

    /**
     * Cache (Desafio): ultima resposta REAL da IA para a mesma pergunta
     * normalizada, dentro do prazo de validade.
     */
    Optional<Pergunta> findFirstByTextoNormalizadoAndOrigemAndDataHoraAfterOrderByDataHoraDesc(
            String textoNormalizado, Pergunta.Origem origem, LocalDateTime desde);

    long countByOrigem(Pergunta.Origem origem);

    @Query("select coalesce(avg(p.tempoMs), 0) from Pergunta p where p.origem = :origem")
    double tempoMedioPorOrigem(Pergunta.Origem origem);

    @Query("select coalesce(sum(p.tokensEntrada + p.tokensSaida), 0) from Pergunta p")
    long totalTokens();
}
