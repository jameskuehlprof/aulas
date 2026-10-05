package br.com.exemplo.lojavirtualinterface.modelo;

import java.util.List;

/** Espelha br.com.exemplo.lojavirtual.dto.PedidoRequestDTO (API). */
public class PedidoRequestDTO {

    private Long clienteId;
    private List<ItemPedidoRequestDTO> itens;

    public PedidoRequestDTO() {
    }

    public PedidoRequestDTO(Long clienteId, List<ItemPedidoRequestDTO> itens) {
        this.clienteId = clienteId;
        this.itens = itens;
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
