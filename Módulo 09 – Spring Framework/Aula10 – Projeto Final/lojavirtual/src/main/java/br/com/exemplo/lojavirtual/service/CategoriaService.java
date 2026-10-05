package br.com.exemplo.lojavirtual.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.exemplo.lojavirtual.dto.CategoriaDTO;
import br.com.exemplo.lojavirtual.dto.CategoriaRequestDTO;
import br.com.exemplo.lojavirtual.exception.RecursoNaoEncontradoException;
import br.com.exemplo.lojavirtual.mapper.CategoriaMapper;
import br.com.exemplo.lojavirtual.model.Categoria;
import br.com.exemplo.lojavirtual.repository.CategoriaRepository;

@Service
public class CategoriaService {

    private final CategoriaRepository repository;

    public CategoriaService(CategoriaRepository repository) {
        this.repository = repository;
    }

    public List<CategoriaDTO> listarTodas() {
        return repository.findAll().stream().map(CategoriaMapper::paraDTO).collect(Collectors.toList());
    }

    public CategoriaDTO buscarPorId(Long id) {
        return CategoriaMapper.paraDTO(buscarEntidade(id));
    }

    public CategoriaDTO criar(CategoriaRequestDTO dto) {
        if (repository.findByNomeIgnoreCase(dto.getNome()).isPresent()) {
            throw new IllegalArgumentException("Já existe uma categoria com este nome");
        }
        Categoria categoria = new Categoria(null, dto.getNome());
        return CategoriaMapper.paraDTO(repository.save(categoria));
    }

    public CategoriaDTO atualizar(Long id, CategoriaRequestDTO dto) {
        Categoria categoria = buscarEntidade(id);
        categoria.setNome(dto.getNome());
        return CategoriaMapper.paraDTO(repository.save(categoria));
    }

    public void deletar(Long id) {
        Categoria categoria = buscarEntidade(id);
        repository.delete(categoria);
    }

    Categoria buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria não encontrada: id " + id));
    }
}
