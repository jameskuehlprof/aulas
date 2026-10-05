package br.com.exemplo.lojavirtual.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.exemplo.lojavirtual.dto.CategoriaDTO;
import br.com.exemplo.lojavirtual.model.Categoria;
import br.com.exemplo.lojavirtual.repository.CategoriaRepository;

/**
 * Camada SERVICE: agora com o CRUD completo (Create, Read, Update, Delete)
 * que vamos expor na Aula 03 através dos verbos HTTP corretos.
 */
@Service
public class CategoriaService {

    private final CategoriaRepository repository;

    public CategoriaService(CategoriaRepository repository) {
        this.repository = repository;
    }

    public List<CategoriaDTO> listarTodos(String nomeContem) {
        return repository.findAll().stream()
                .filter(p -> nomeContem == null
                        || p.getNome().toLowerCase().contains(nomeContem.toLowerCase()))
                .map(this::paraDTO)
                .collect(Collectors.toList());
    }

    public Optional<CategoriaDTO> buscarPorId(Long id) {
        return repository.findById(id).map(this::paraDTO);
    }

    public CategoriaDTO criar(CategoriaDTO dto) {
        Categoria categoria = new Categoria(null, dto.getNome());
        Categoria salvo = repository.save(categoria);
        return paraDTO(salvo);
    }

    public Optional<CategoriaDTO> atualizar(Long id, CategoriaDTO dto) {
        return repository.findById(id).map(produtoExistente -> {
            produtoExistente.setNome(dto.getNome());           
            return paraDTO(repository.save(produtoExistente));
        });
    }

    public Optional<CategoriaDTO> atualizarNome(Long id, String novoNome) {
        return repository.findById(id).map(categoriaExistente -> {
        	categoriaExistente.setNome(novoNome);
            return paraDTO(repository.save(categoriaExistente));
        });
    }

    public boolean deletar(Long id) {
        boolean existia = repository.findById(id).isPresent();
        repository.deleteById(id);
        return existia;
    }

    private CategoriaDTO paraDTO(Categoria categoria) {
        return new CategoriaDTO(categoria.getId(), categoria.getNome());
    }
}
