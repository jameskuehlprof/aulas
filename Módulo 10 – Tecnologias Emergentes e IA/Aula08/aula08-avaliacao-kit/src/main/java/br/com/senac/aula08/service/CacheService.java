package br.com.senac.aula08.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import br.com.senac.aula08.config.IaProperties;
import br.com.senac.aula08.model.Ticket;
import br.com.senac.aula08.repository.TicketRepository;

/**
 * INCREMENTO 5 (Aula 07): cache de respostas.
 * Use TicketRepository.findFirstByTextoNormalizadoAndOrigemAndDataHoraAfterOrderByDataHoraDesc(...)
 * com origem IA e validade ia.cache.validade-minutos. Logue CACHE HIT / MISS.
 */
@Service
public class CacheService {

    private static final Logger log = LoggerFactory.getLogger(CacheService.class);

    private final TicketRepository repo;
    private final IaProperties props;

    public CacheService(TicketRepository repo, IaProperties props) {
        this.repo = repo;
        this.props = props;
    }

    public boolean ativo() {
        return props.cache().ativo();
    }

    public Optional<Ticket> buscar(String textoNormalizado) {
        // TODO Incremento 5
        return Optional.empty();
    }

    public static String normalizar(String texto) {
        return ClassificadorRegras.normalizar(texto);
    }
}
