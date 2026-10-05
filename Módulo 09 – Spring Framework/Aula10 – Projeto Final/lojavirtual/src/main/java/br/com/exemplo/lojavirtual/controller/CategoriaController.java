package br.com.exemplo.lojavirtual.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.exemplo.lojavirtual.dto.CategoriaDTO;
import br.com.exemplo.lojavirtual.dto.CategoriaRequestDTO;
import br.com.exemplo.lojavirtual.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Categorias", description = "Cadastro e consulta de categorias de produto")
@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService service;

    public CategoriaController(CategoriaService service) {
        this.service = service;
    }

    @Operation(summary = "Lista todas as categorias")
    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> listar() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @Operation(summary = "Busca uma categoria pelo id")
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> buscarPorId(
            @Parameter(description = "Id da categoria") @PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Cadastra uma nova categoria")
    @PostMapping
    public ResponseEntity<CategoriaDTO> criar(@Valid @RequestBody CategoriaRequestDTO dto) {
        return ResponseEntity.status(201).body(service.criar(dto));
    }

    @Operation(summary = "Atualiza uma categoria existente")
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDTO> atualizar(
            @Parameter(description = "Id da categoria") @PathVariable Long id,
            @Valid @RequestBody CategoriaRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @Operation(summary = "Remove uma categoria")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@Parameter(description = "Id da categoria") @PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
