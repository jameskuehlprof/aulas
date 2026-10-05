package br.com.exemplo.lojavirtual.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.exemplo.lojavirtual.dto.PedidoDTO;
import br.com.exemplo.lojavirtual.dto.PedidoRequestDTO;
import br.com.exemplo.lojavirtual.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Pedidos", description = "Criação e consulta de pedidos de compra")
@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @Operation(summary = "Lista todos os pedidos")
    @GetMapping
    public ResponseEntity<List<PedidoDTO>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @Operation(summary = "Lista os pedidos de um cliente")
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<PedidoDTO>> listarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(service.listarPorCliente(clienteId));
    }

    @Operation(summary = "Busca um pedido por id")
    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // Desafio extra do exercício: documentando explicitamente os possíveis
    // códigos de resposta deste endpoint, além do resumo em @Operation.
    @Operation(
            summary = "Cria um novo pedido, combinando um cliente e um ou mais produtos existentes",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos (ex.: lista de itens vazia ou quantidade <= 0)"),
                    @ApiResponse(responseCode = "404", description = "Cliente ou algum produto informado não existe")
            })
    @PostMapping
    public ResponseEntity<PedidoDTO> criar(@Valid @RequestBody PedidoRequestDTO dto) {
        return ResponseEntity.status(201).body(service.criar(dto));
    }

    @Operation(summary = "Remove um pedido (e seus itens, em cascata)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
