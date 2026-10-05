package br.com.exemplo.lojavirtual.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.exemplo.lojavirtual.dto.ProdutoDTO;
import br.com.exemplo.lojavirtual.dto.ProdutoRequestDTO;
import br.com.exemplo.lojavirtual.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Aula 7 — Controller de referência, totalmente documentado com anotações
 * do springdoc-openapi. @Tag agrupa os endpoints na tela do Swagger UI;
 * @Operation descreve cada um. O restante dos Controllers do projeto pode
 * (e deve, como exercício) receber o mesmo tratamento.
 */
@Tag(name = "Produtos", description = "Cadastro e consulta de produtos da loja")
@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    @Operation(summary = "Lista produtos", description = "Lista todos os produtos, com filtro opcional por nome")
    @GetMapping
    public ResponseEntity<List<ProdutoDTO>> listar(
            @Parameter(description = "Filtro por parte do nome (opcional)")
            @RequestParam(required = false) String nome) {
        return ResponseEntity.ok(service.listarTodos(nome));
    }

    @Operation(summary = "Busca um produto por id")
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Cria um novo produto")
    @PostMapping
    public ResponseEntity<ProdutoDTO> criar(@Valid @RequestBody ProdutoRequestDTO dto) {
        return ResponseEntity.status(201).body(service.criar(dto));
    }

    @Operation(summary = "Atualiza um produto por completo")
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoDTO> atualizar(@PathVariable Long id, @Valid @RequestBody ProdutoRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @Operation(summary = "Atualiza somente o preço do produto")
    @PatchMapping("/{id}/preco")
    public ResponseEntity<ProdutoDTO> atualizarPreco(@PathVariable Long id, @RequestParam BigDecimal novoPreco) {
        return ResponseEntity.ok(service.atualizarPreco(id, novoPreco));
    }

    @Operation(summary = "Repõe estoque de um produto",
            description = "Soma a quantidade informada ao estoque atual do produto. "
                    + "Quantidade precisa ser maior que zero.")
    @PatchMapping("/{id}/repor-estoque")
    public ResponseEntity<ProdutoDTO> reporEstoque(
            @PathVariable Long id,
            @Parameter(description = "Quantidade a somar ao estoque atual (> 0)")
            @RequestParam Integer quantidade) {
        return ResponseEntity.ok(service.reporEstoque(id, quantidade));
    }

    @Operation(summary = "Lista produtos com estoque abaixo do mínimo (desafio extra)")
    @GetMapping("/estoque-baixo")
    public ResponseEntity<List<ProdutoDTO>> listarComEstoqueBaixo() {
        return ResponseEntity.ok(service.listarComEstoqueBaixo());
    }

    @Operation(summary = "Remove um produto")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
