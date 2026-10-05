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
import br.com.exemplo.lojavirtual.service.ProdutoService;

/**
 * Aula 03 — agora com o CRUD REST completo:
 *   GET (coleção e por id), POST, PUT, PATCH e DELETE,
 * usando ResponseEntity para controlar o status HTTP de cada resposta.
 */
@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    // GET /produtos              -> 200, lista completa
    // GET /produtos?nome=teclado -> 200, lista filtrada (RequestParam OPCIONAL)
    @GetMapping
    public ResponseEntity<List<ProdutoDTO>> listar(
            @RequestParam(required = false) String nome) {
        return ResponseEntity.ok(service.listarTodos(nome));
    }

    // GET /produtos/{id} -> 200 com o produto, ou 404 se não existir
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDTO> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST /produtos -> 201 Created, corpo da requisição em JSON (RequestBody)
    @PostMapping
    public ResponseEntity<ProdutoDTO> criar(@RequestBody ProdutoDTO dto) {
        ProdutoDTO criado = service.criar(dto);
        return ResponseEntity.status(201).body(criado);
    }

    // PUT /produtos/{id} -> substitui nome e preço do produto por completo
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoDTO> atualizar(@PathVariable Long id, @RequestBody ProdutoDTO dto) {
        return service.atualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // PATCH /produtos/{id}/preco?novoPreco=99.90 -> atualização PARCIAL
    // (só o preço muda; PUT muda o recurso inteiro, PATCH muda uma parte)
    @PatchMapping("/{id}/preco")
    public ResponseEntity<ProdutoDTO> atualizarPreco(
            @PathVariable Long id, @RequestParam BigDecimal novoPreco) {
        return service.atualizarPreco(id, novoPreco)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE /produtos/{id} -> 204 (sem corpo) se removeu, 404 se não existia
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        boolean removido = service.deletar(id);
        return removido ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
