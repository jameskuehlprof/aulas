package br.com.exemplo.lojavirtual.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.exemplo.lojavirtual.dto.ProdutoDTO;
import br.com.exemplo.lojavirtual.dto.ProdutoRequestDTO;
import br.com.exemplo.lojavirtual.exception.RecursoNaoEncontradoException;
import br.com.exemplo.lojavirtual.mapper.ProdutoMapper;
import br.com.exemplo.lojavirtual.model.Categoria;
import br.com.exemplo.lojavirtual.model.Produto;
import br.com.exemplo.lojavirtual.repository.ProdutoRepository;

@Service
@Transactional(readOnly = true)
public class ProdutoService {

    private final ProdutoRepository repository;
    private final CategoriaService categoriaService;

    public ProdutoService(ProdutoRepository repository, CategoriaService categoriaService) {
        this.repository = repository;
        this.categoriaService = categoriaService;
    }

    public List<ProdutoDTO> listarTodos(String nomeContem) {
        List<Produto> produtos = (nomeContem == null || nomeContem.isBlank())
                ? repository.findAll()
                : repository.findByNomeContainingIgnoreCase(nomeContem);
        return produtos.stream().map(ProdutoMapper::paraDTO).collect(Collectors.toList());
    }

    public ProdutoDTO buscarPorId(Long id) {
        return ProdutoMapper.paraDTO(buscarEntidade(id));
    }

    @Transactional
    public ProdutoDTO criar(ProdutoRequestDTO dto) {
        Categoria categoria = categoriaService.buscarEntidade(dto.getCategoriaId());
        // Produto novo sempre nasce com estoque zerado; a reposição é feita
        // depois, através do endpoint /produtos/{id}/repor-estoque.
        Produto produto = new Produto(null, dto.getNome(), dto.getPreco(), 0, dto.getEstoqueMinimo());
        produto.setCategoria(categoria);
        return ProdutoMapper.paraDTO(repository.save(produto));
    }

    @Transactional
    public ProdutoDTO atualizar(Long id, ProdutoRequestDTO dto) {
        Produto produto = buscarEntidade(id);
        Categoria categoria = categoriaService.buscarEntidade(dto.getCategoriaId());
        produto.setNome(dto.getNome());
        produto.setPreco(dto.getPreco());
        produto.setEstoqueMinimo(dto.getEstoqueMinimo());
        produto.setCategoria(categoria);
        return ProdutoMapper.paraDTO(repository.save(produto));
    }

    @Transactional
    public ProdutoDTO reporEstoque(Long id, Integer quantidade) {
        if (quantidade == null || quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade a repor deve ser maior que zero");
        }
        Produto produto = buscarEntidade(id);
        produto.setQuantidadeEmEstoque(produto.getQuantidadeEmEstoque() + quantidade);
        return ProdutoMapper.paraDTO(repository.save(produto));
    }

    public List<ProdutoDTO> listarComEstoqueBaixo() {
        return repository.findAll().stream()
                .filter(produto -> produto.getQuantidadeEmEstoque() < produto.getEstoqueMinimo())
                .map(ProdutoMapper::paraDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProdutoDTO atualizarPreco(Long id, BigDecimal novoPreco) {
        Produto produto = buscarEntidade(id);
        produto.setPreco(novoPreco);
        return ProdutoMapper.paraDTO(repository.save(produto));
    }

    @Transactional
    public void deletar(Long id) {
        Produto produto = buscarEntidade(id);
        repository.delete(produto);
    }

    Produto buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado: id " + id));
    }

}
