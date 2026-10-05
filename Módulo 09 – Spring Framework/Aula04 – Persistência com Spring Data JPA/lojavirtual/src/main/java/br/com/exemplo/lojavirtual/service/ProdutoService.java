package br.com.exemplo.lojavirtual.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.exemplo.lojavirtual.dto.ProdutoDTO;
import br.com.exemplo.lojavirtual.model.Produto;
import br.com.exemplo.lojavirtual.repository.ProdutoRepository;

/**
 * Boa notícia: o ProdutoService quase não muda! Ele continua chamando
 * repository.findAll(), repository.findById(), repository.save() e
 * repository.deleteById() exatamente como antes — só que agora essas
 * chamadas vão até o banco MySQL de verdade, via Spring Data JPA.
 *
 * Isso comprova, na prática, o motivo de isolar a camada Repository:
 * trocamos TODA a forma de persistência (memória -> banco relacional) sem
 * mudar uma linha sequer do Controller, e quase nada do Service.
 */
@Service
public class ProdutoService {

    private final ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    public List<ProdutoDTO> listarTodos(String nomeContem) {
        List<Produto> produtos = (nomeContem == null || nomeContem.isBlank())
                ? repository.findAll()
                : repository.findByNomeContainingIgnoreCase(nomeContem);
        return produtos.stream().map(this::paraDTO).collect(Collectors.toList());
    }

    public Optional<ProdutoDTO> buscarPorId(Long id) {
        return repository.findById(id).map(this::paraDTO);
    }

    public ProdutoDTO criar(ProdutoDTO dto) {
        Produto produto = new Produto(null, dto.getNome(), dto.getPreco(), 0);
        Produto salvo = repository.save(produto);
        return paraDTO(salvo);
    }

    public Optional<ProdutoDTO> atualizar(Long id, ProdutoDTO dto) {
        return repository.findById(id).map(produtoExistente -> {
            produtoExistente.setNome(dto.getNome());
            produtoExistente.setPreco(dto.getPreco());
            return paraDTO(repository.save(produtoExistente));
        });
    }

    public Optional<ProdutoDTO> atualizarPreco(Long id, BigDecimal novoPreco) {
        return repository.findById(id).map(produtoExistente -> {
            produtoExistente.setPreco(novoPreco);
            return paraDTO(repository.save(produtoExistente));
        });
    }

    public boolean deletar(Long id) {
        boolean existia = repository.existsById(id);
        if (existia) {
            repository.deleteById(id);
        }
        return existia;
    }

    private ProdutoDTO paraDTO(Produto produto) {
        return new ProdutoDTO(produto.getId(), produto.getNome(), produto.getPreco());
    }
}
