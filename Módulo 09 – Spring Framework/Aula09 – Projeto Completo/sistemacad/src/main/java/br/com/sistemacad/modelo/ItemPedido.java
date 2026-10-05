package br.com.sistemacad.modelo;

/**
 * Representa um item dentro de um pedido (relação 1:N com Pedido).
 *
 * Um ItemPedido nunca existe sozinho: ele sempre pertence a um Pedido
 * (pedidoId), da mesma forma que, no banco, a tabela item_pedido tem uma
 * chave estrangeira obrigatória para pedido.id.
 *
 * Classe simples (POJO) — nenhuma regra de acesso a banco de dados aqui,
 * essa responsabilidade é do PedidoDAO.
 */
public class ItemPedido {

    private int id;
    private int pedidoId;
    private String produto;
    private int quantidade;
    private double precoUnitario;

    public ItemPedido() {
    }

    public ItemPedido(String produto, int quantidade, double precoUnitario) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(int pedidoId) {
        this.pedidoId = pedidoId;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    /**
     * Subtotal do item = quantidade x preço unitário.
     * Calculado em Java (nunca confiar em um total enviado pelo formulário).
     */
    public double getSubtotal() {
        return quantidade * precoUnitario;
    }

    @Override
    public String toString() {
        return "ItemPedido{produto='" + produto + "', quantidade=" + quantidade
                + ", precoUnitario=" + precoUnitario + "}";
    }
}
