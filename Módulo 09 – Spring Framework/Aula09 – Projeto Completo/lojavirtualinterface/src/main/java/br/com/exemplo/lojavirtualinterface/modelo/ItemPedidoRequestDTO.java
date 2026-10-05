package br.com.exemplo.lojavirtualinterface.modelo;

/** Espelha br.com.exemplo.lojavirtual.dto.ItemPedidoRequestDTO (API). */
public class ItemPedidoRequestDTO {

    private Long produtoId;
    private Integer quantidade;

    public ItemPedidoRequestDTO() {
    }

    public ItemPedidoRequestDTO(Long produtoId, Integer quantidade) {
        this.produtoId = produtoId;
        this.quantidade = quantidade;
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
