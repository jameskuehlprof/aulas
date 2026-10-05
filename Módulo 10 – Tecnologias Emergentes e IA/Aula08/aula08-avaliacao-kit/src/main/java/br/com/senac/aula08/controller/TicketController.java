package br.com.senac.aula08.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.senac.aula08.service.TicketService;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * KIT DO ALUNO - voce cria os endpoints (Controller FINO: recebe, valida, chama o Service, devolve).
 *
 *   POST /api/tickets             {usuarioId, texto} -> 201 + TicketResponse      (Incrementos 1, 2, 3, 5)
 *   GET  /api/tickets/{id}                            -> TicketResponse            (Incremento 2)
 *   POST /api/tickets/{id}/foto   multipart "imagem"  -> TicketResponse            (Incremento 4)
 *   GET  /api/metricas                                -> MetricasResponse          (Incremento 5)
 *
 * (GET /api/usuarios/{id}/tickets ja existe em UsuarioController.)
 * Use @Valid nos DTOs, @ResponseStatus(HttpStatus.CREATED) no POST e @Operation do Swagger.
 */
@RestController
@RequestMapping("/api")
@Tag(name = "2. Tickets")
public class TicketController {

    private final TicketService service;

    public TicketController(TicketService service) {
        this.service = service;
    }

    // TODO Incremento 1: POST /tickets

    // TODO Incremento 2: GET /tickets/{id}

    // TODO Incremento 4: POST /tickets/{id}/foto (consumes = MediaType.MULTIPART_FORM_DATA_VALUE, @RequestPart("imagem") MultipartFile)

    // TODO Incremento 5: GET /metricas
}
