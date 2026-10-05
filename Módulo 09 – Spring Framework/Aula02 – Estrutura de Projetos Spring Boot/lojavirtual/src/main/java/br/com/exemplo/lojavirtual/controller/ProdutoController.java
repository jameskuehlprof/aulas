package br.com.exemplo.lojavirtual.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.exemplo.lojavirtual.dto.ProdutoDTO;
import br.com.exemplo.lojavirtual.service.ProdutoService;

/**
 * Camada CONTROLLER: é a "porta de entrada" da API — recebe a requisição
 * HTTP e devolve uma resposta. Repare que o Controller não tem NENHUMA
 * regra de negócio: ele apenas delega para o ProdutoService.
 *
 * @RequestMapping("/produtos") define um prefixo comum para todos os
 * endpoints desta classe — assim não repetimos "/produtos" em cada método.
 */
@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProdutoDTO> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public ProdutoDTO buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + id));
    }
}
