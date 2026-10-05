package br.com.exemplo.lojavirtual.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ItemPedidoRequestDTO {

    @NotNull(message = "o produtoId é obrigatório")
    private Long produtoId;

    @NotNull(message = "a quantidade é obrigatória")
    @Positive(message = "a quantidade deve ser maior que zero")
    private Integer quantidade;

    public ItemPedidoRequestDTO() {
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}
