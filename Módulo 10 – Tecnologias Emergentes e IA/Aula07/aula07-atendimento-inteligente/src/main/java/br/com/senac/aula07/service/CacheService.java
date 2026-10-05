package br.com.senac.aula07.service;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import br.com.senac.aula07.config.IaProperties;
import br.com.senac.aula07.model.Pergunta;
import br.com.senac.aula07.repository.PerguntaRepository;

/**
 * DESAFIO - Cache inteligente de respostas.
 *
 * Criterio de "mesma pergunta": texto NORMALIZADO igual (minusculas, sem
 * acentos, sem pontuacao, espacos unicos). Assim "Vocês trocam tela?" e
 * "voces trocam tela" batem.
 *
 * O cache mora no proprio banco (tabela pergunta, coluna texto_normalizado):
 * nao precisa de Redis nem de Map em memoria, sobrevive a restart e ja fica
 * auditavel. Validade configuravel em ia.cache.validade-minutos.
 */
@Service
public class CacheService {

    private static final Logger log = LoggerFactory.getLogger(CacheService.class);

    private final PerguntaRepository repo;
    private final IaProperties props;

    public CacheService(PerguntaRepository repo, IaProperties props) {
        this.repo = repo;
        this.props = props;
    }

    public boolean ativo() {
        return props.cache().ativo();
    }

    /** Devolve a resposta em cache, se houver uma valida. */
    public Optional<Pergunta> buscar(String textoNormalizado) {
        if (!ativo()) return Optional.empty();
        LocalDateTime desde = LocalDateTime.now().minusMinutes(props.cache().validadeMinutos());
        Optional<Pergunta> hit = repo.findFirstByTextoNormalizadoAndOrigemAndDataHoraAfterOrderByDataHoraDesc(
                textoNormalizado, Pergunta.Origem.IA, desde);
        if (hit.isPresent()) {
            log.info("CACHE HIT  -> '{}' (pergunta #{} de {})", textoNormalizado, hit.get().getId(), hit.get().getDataHora());
        } else {
            log.info("CACHE MISS -> '{}'", textoNormalizado);
        }
        return hit;
    }

    /** "  Vocês TROCAM tela?? " -> "voces trocam tela" */
    public static String normalizar(String texto) {
        String semAcento = Normalizer.normalize(texto, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        return semAcento.toLowerCase()
                .replaceAll("[^\\p{L}\\p{N}\\s]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }
}
