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

import br.com.exemplo.lojavirtual.dto.ClienteDTO;
import br.com.exemplo.lojavirtual.dto.ClienteRequestDTO;
import br.com.exemplo.lojavirtual.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Clientes", description = "Cadastro e consulta de clientes")
@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @Operation(summary = "Lista todos os clientes")
    @GetMapping
    public ResponseEntity<List<ClienteDTO>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @Operation(summary = "Busca um cliente pelo id")
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> buscarPorId(
            @Parameter(description = "Id do cliente") @PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Cadastra um novo cliente")
    @PostMapping
    public ResponseEntity<ClienteDTO> criar(@Valid @RequestBody ClienteRequestDTO dto) {
        return ResponseEntity.status(201).body(service.criar(dto));
    }

    @Operation(summary = "Atualiza um cliente existente")
    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> atualizar(
            @Parameter(description = "Id do cliente") @PathVariable Long id,
            @Valid @RequestBody ClienteRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @Operation(summary = "Remove um cliente")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@Parameter(description = "Id do cliente") @PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
