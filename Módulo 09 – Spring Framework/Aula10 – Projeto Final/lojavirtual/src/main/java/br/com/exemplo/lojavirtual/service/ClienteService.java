package br.com.exemplo.lojavirtual.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.exemplo.lojavirtual.dto.ClienteDTO;
import br.com.exemplo.lojavirtual.dto.ClienteRequestDTO;
import br.com.exemplo.lojavirtual.exception.RecursoNaoEncontradoException;
import br.com.exemplo.lojavirtual.mapper.ClienteMapper;
import br.com.exemplo.lojavirtual.model.Cliente;
import br.com.exemplo.lojavirtual.repository.ClienteRepository;

@Service
public class ClienteService {

    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public List<ClienteDTO> listarTodos() {
        return repository.findAll().stream().map(ClienteMapper::paraDTO).collect(Collectors.toList());
    }

    public ClienteDTO buscarPorId(Long id) {
        return ClienteMapper.paraDTO(buscarEntidade(id));
    }

    public ClienteDTO criar(ClienteRequestDTO dto) {
        if (repository.findByCpf(dto.getCpf()).isPresent()) {
            throw new IllegalArgumentException("Já existe um cliente cadastrado com este CPF");
        }
        Cliente cliente = new Cliente(null, dto.getNome(), dto.getEmail(), dto.getCpf());
        return ClienteMapper.paraDTO(repository.save(cliente));
    }

    public ClienteDTO atualizar(Long id, ClienteRequestDTO dto) {
        Cliente cliente = buscarEntidade(id);
        cliente.setNome(dto.getNome());
        cliente.setEmail(dto.getEmail());
        cliente.setCpf(dto.getCpf());
        return ClienteMapper.paraDTO(repository.save(cliente));
    }

    public void deletar(Long id) {
        Cliente cliente = buscarEntidade(id);
        repository.delete(cliente);
    }

    Cliente buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado: id " + id));
    }

}
