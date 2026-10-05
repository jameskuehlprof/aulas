package br.com.exemplo.lojavirtual.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.exemplo.lojavirtual.dto.ProdutoDTO;
import br.com.exemplo.lojavirtual.dto.ProdutoRequestDTO;
import br.com.exemplo.lojavirtual.exception.RecursoNaoEncontradoException;
import br.com.exemplo.lojavirtual.model.Produto;
import br.com.exemplo.lojavirtual.repository.ProdutoRepository;

/**
 * Aula 5: o Service agora lança RecursoNaoEncontradoException em vez de
 * devolver Optional — quem decide o que fazer com "não encontrado" passa a
 * ser o GlobalExceptionHandler, de forma centralizada para toda a API.
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

    public ProdutoDTO buscarPorId(Long id) {
        return paraDTO(buscarEntidade(id));
    }

    public ProdutoDTO criar(ProdutoRequestDTO dto) {
        Produto produto = new Produto(null, dto.getNome(), dto.getPreco(), 0);
        return paraDTO(repository.save(produto));
    }

    public ProdutoDTO atualizar(Long id, ProdutoRequestDTO dto) {
        Produto produto = buscarEntidade(id);
        produto.setNome(dto.getNome());
        produto.setPreco(dto.getPreco());
        return paraDTO(repository.save(produto));
    }

    public ProdutoDTO atualizarPreco(Long id, BigDecimal novoPreco) {
        Produto produto = buscarEntidade(id);
        produto.setPreco(novoPreco);
        return paraDTO(repository.save(produto));
    }

    public void deletar(Long id) {
        Produto produto = buscarEntidade(id);
        repository.delete(produto);
    }

    private Produto buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado: id " + id));
    }

    private ProdutoDTO paraDTO(Produto produto) {
        return new ProdutoDTO(produto.getId(), produto.getNome(), produto.getPreco());
    }
}
