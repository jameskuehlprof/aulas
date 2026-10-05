package br.com.exemplo.lojavirtual.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.exemplo.lojavirtual.dto.ProdutoDTO;
import br.com.exemplo.lojavirtual.model.Produto;
import br.com.exemplo.lojavirtual.repository.ProdutoRepository;

/**
 * Camada SERVICE: contém as regras de negócio da aplicação.
 *
 * O Controller NUNCA deve falar diretamente com o Repository — quem faz
 * essa ponte é o Service. Isso mantém o Controller "magro" (só trata a
 * requisição/resposta HTTP) e concentra as regras de negócio em um único
 * lugar, fácil de testar e reaproveitar.
 */
@Service
public class ProdutoService {

    private final ProdutoRepository repository;

    // Injeção de Dependência via construtor (o mesmo conceito da Aula 01):
    // o Spring cria o ProdutoRepository e "entrega" pronto para o Service.
    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    public List<ProdutoDTO> listarTodos() {
        return repository.findAll().stream()
                .map(this::paraDTO)
                .collect(Collectors.toList());
    }

    public Optional<ProdutoDTO> buscarPorId(Long id) {
        return repository.findById(id).map(this::paraDTO);
    }

    private ProdutoDTO paraDTO(Produto produto) {
        return new ProdutoDTO(produto.getId(), produto.getNome(), produto.getPreco());
    }
}
