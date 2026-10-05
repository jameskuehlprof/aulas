package br.com.senac.aula07.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.senac.aula07.dto.Dtos.MetricasResponse;
import br.com.senac.aula07.dto.Dtos.PerguntaRequest;
import br.com.senac.aula07.dto.Dtos.PerguntaResponse;
import br.com.senac.aula07.service.AtendimentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Controller FINO: recebe JSON, valida, chama o Service, devolve JSON.
 * Nenhuma regra de negocio e nenhuma chamada de IA aqui.
 */
@RestController
@RequestMapping("/api")
@Tag(name = "2. Perguntas", description = "Atendimento com IA, cache e metricas")
public class PerguntaController {

    private final AtendimentoService service;

    public PerguntaController(AtendimentoService service) {
        this.service = service;
    }

    @PostMapping("/perguntas")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Exercicio 1 - envia uma pergunta e recebe a resposta gerada pela IA (ou cache/fallback)")
    public PerguntaResponse perguntar(@Valid @RequestBody PerguntaRequest req) {
        return service.perguntar(req.usuarioId(), req.texto());
    }

    @GetMapping("/metricas")
    @Operation(summary = "Desafio - cache hit x miss, fallbacks, tempo medio e tokens")
    public MetricasResponse metricas() {
        return service.metricas();
    }
}
