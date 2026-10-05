package br.com.exemplo.lojavirtual.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.exemplo.lojavirtual.dto.RelatorioDTO;
import br.com.exemplo.lojavirtual.service.RelatorioService;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    private final RelatorioService service;

    public RelatorioController(RelatorioService service) {
        this.service = service;
    }

    @GetMapping("/total-por-categoria")
    public ResponseEntity<List<RelatorioDTO>> totalPorCategoria() {
        return ResponseEntity.ok(service.totalVendidoPorCategoria());
    }
}
