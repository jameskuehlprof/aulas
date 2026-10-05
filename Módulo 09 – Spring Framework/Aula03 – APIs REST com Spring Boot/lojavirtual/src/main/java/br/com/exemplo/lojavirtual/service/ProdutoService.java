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
 * Camada SERVICE: agora com o CRUD completo (Create, Read, Update, Delete)
 * que vamos expor na Aula 03 através dos verbos HTTP corretos.
 */
@Service
public class ProdutoService {

    private final ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    public List<ProdutoDTO> listarTodos(String nomeContem) {
        return repository.findAll().stream()
                .filter(p -> nomeContem == null
                        || p.getNome().toLowerCase().contains(nomeContem.toLowerCase()))
                .map(this::paraDTO)
                .collect(Collectors.toList());
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
        boolean existia = repository.findById(id).isPresent();
        repository.deleteById(id);
        return existia;
    }

    private ProdutoDTO paraDTO(Produto produto) {
        return new ProdutoDTO(produto.getId(), produto.getNome(), produto.getPreco());
    }
}
