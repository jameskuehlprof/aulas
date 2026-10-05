package br.com.exemplo.lojavirtual.mapper;

import br.com.exemplo.lojavirtual.dto.ClienteDTO;
import br.com.exemplo.lojavirtual.model.Cliente;

public final class ClienteMapper {

    private ClienteMapper() {
    }

    public static ClienteDTO paraDTO(Cliente cliente) {
        return new ClienteDTO(cliente.getId(), cliente.getNome(), cliente.getEmail());
    }
}
