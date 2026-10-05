package br.com.exemplo.lojavirtual.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PedidoDTO {

    private Long id;
    private Long clienteId;
    private String clienteNome;
    private LocalDateTime dataPedido;
    private List<ItemPedidoDTO> itens;
    private BigDecimal total;

    public PedidoDTO() {
    }

    public PedidoDTO(Long id, Long clienteId, String clienteNome, LocalDateTime dataPedido,
            List<ItemPedidoDTO> itens, BigDecimal total) {
        this.id = id;
        this.clienteId = clienteId;
        this.clienteNome = clienteNome;
        this.dataPedido = dataPedido;
        this.itens = itens;
        this.total = total;
    }

    public Long getId() {
        return id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public String getClienteNome() {
        return clienteNome;
    }

    public LocalDateTime getDataPedido() {
        return dataPedido;
    }

    public List<ItemPedidoDTO> getItens() {
        return itens;
    }

    public BigDecimal getTotal() {
        return total;
    }
}
