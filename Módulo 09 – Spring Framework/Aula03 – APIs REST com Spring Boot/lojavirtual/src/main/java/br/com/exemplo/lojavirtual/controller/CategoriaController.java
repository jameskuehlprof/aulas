package br.com.exemplo.lojavirtual.controller;

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

import br.com.exemplo.lojavirtual.dto.CategoriaDTO;

import br.com.exemplo.lojavirtual.service.CategoriaService;

/**
 * Aula 03 — agora com o CRUD REST completo:
 *   GET (coleção e por id), POST, PUT, PATCH e DELETE,
 * usando ResponseEntity para controlar o status HTTP de cada resposta.
 */
@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService service;

    public CategoriaController(CategoriaService service) {
        this.service = service;
    }

    // GET /categorias              -> 200, lista completa
    // GET /categorias?nome=informática -> 200, lista filtrada (RequestParam OPCIONAL)
    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> listar(
            @RequestParam(required = false) String nome) {
        return ResponseEntity.ok(service.listarTodos(nome));
    }

    // GET /categorias/{id} -> 200 com o categoria, ou 404 se não existir
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST /categorias -> 201 Created, corpo da requisição em JSON (RequestBody)
    @PostMapping
    public ResponseEntity<CategoriaDTO> criar(@RequestBody CategoriaDTO dto) {
        CategoriaDTO criado = service.criar(dto);
        return ResponseEntity.status(201).body(criado);
    }

    // PUT /categorias/{id} -> substitui nome e categoria por completo
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDTO> atualizar(@PathVariable Long id, @RequestBody CategoriaDTO dto) {
        return service.atualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // PATCH /categorias/{id}/nome?novoNome=teste -> atualização PARCIAL
    // (só o preço muda; PUT muda o recurso inteiro, PATCH muda uma parte)
    @PatchMapping("/{id}/nome")  
    public ResponseEntity<CategoriaDTO> atualizarNome(
            @PathVariable Long id, @RequestParam String novoNome) {
        return service.atualizarNome(id, novoNome)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE /categorias/{id} -> 204 (sem corpo) se removeu, 404 se não existia
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        boolean removido = service.deletar(id);
        return removido ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
