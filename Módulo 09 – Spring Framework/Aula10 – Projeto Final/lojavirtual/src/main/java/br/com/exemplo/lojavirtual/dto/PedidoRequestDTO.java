package br.com.exemplo.lojavirtual.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class PedidoRequestDTO {

    @NotNull(message = "o clienteId é obrigatório")
    private Long clienteId;

    @NotEmpty(message = "o pedido precisa ter ao menos um item")
    @Valid
    private List<ItemPedidoRequestDTO> itens;

    public PedidoRequestDTO() {
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public List<ItemPedidoRequestDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedidoRequestDTO> itens) {
        this.itens = itens;
    }
}
