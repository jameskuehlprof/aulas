package br.com.sistemacad.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa um pedido feito por um cliente.
 *
 * Relacionamento (chave estrangeira):
 *
 *   Cliente (1) ────────< Pedido (N) ────────< ItemPedido (N)
 *
 * Ou seja: um cliente pode ter vários pedidos, e cada pedido pode ter
 * vários itens. No banco, isso é implementado com duas chaves
 * estrangeiras: pedido.cliente_id -> cliente.id e
 * item_pedido.pedido_id -> pedido.id.
 *
 * clienteNome não existe na tabela pedido: é preenchido pelo PedidoDAO a
 * partir de um JOIN com a tabela cliente, apenas para facilitar a exibição
 * nas telas (listagem), evitando um SELECT extra para cada linha.
 */
public class Pedido {

    public static final String STATUS_ABERTO = "ABERTO";
    public static final String STATUS_PAGO = "PAGO";
    public static final String STATUS_CANCELADO = "CANCELADO";

    private int id;
    private int clienteId;
    private String clienteNome;
    private LocalDate dataPedido;
    private String status = STATUS_ABERTO;
    private double valorTotal;
    private List<ItemPedido> itens = new ArrayList<>();

    public Pedido() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public String getClienteNome() {
        return clienteNome;
    }

    public void setClienteNome(String clienteNome) {
        this.clienteNome = clienteNome;
    }

    public LocalDate getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDate dataPedido) {
        this.dataPedido = dataPedido;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = (itens != null) ? itens : new ArrayList<>();
    }

    /**
     * Soma o subtotal de todos os itens do pedido.
     * Usado pelo PedidoDAO para preencher valor_total antes de gravar,
     * em vez de confiar em um total enviado pronto pelo formulário.
     */
    public double calcularValorTotal() {
        double total = 0;
        for (ItemPedido item : itens) {
            total += item.getSubtotal();
        }
        return total;
    }

    @Override
    public String toString() {
        return "Pedido{id=" + id + ", clienteId=" + clienteId + ", status='" + status
                + "', itens=" + itens.size() + "}";
    }
}
