package br.com.exemplo.lojavirtual.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;


/**
 * Pedido é o lado "UM" de dois relacionamentos diferentes:
 *  - Cliente (1) -> Pedido (N): um cliente pode ter vários pedidos;
 *  - Pedido (1) -> ItemPedido (N): um pedido tem vários itens.
 *
 * O "N:N" entre Pedido e Produto (um pedido tem vários produtos, um produto
 * aparece em vários pedidos) é implementado através de ItemPedido — uma
 * ENTIDADE ASSOCIATIVA, e não da anotação @ManyToMany direta. Na prática
 * profissional isso é preferível ao @ManyToMany puro, porque quase sempre
 * a relação carrega dados próprios (aqui: quantidade e precoUnitario no
 * momento da compra).
 *
 * cascade = CascadeType.ALL + orphanRemoval = true em itens: se eu excluir
 * o Pedido, os ItemPedido somem junto (faz sentido: um item não existe sem
 * o pedido). Se eu remover um item da lista "itens" em memória e salvar o
 * Pedido, o Hibernate também apaga essa linha do banco.
 */
@Entity
@jakarta.persistence.Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    private LocalDateTime dataPedido = LocalDateTime.now();

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>();

    public Pedido() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDateTime getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDateTime dataPedido) {
        this.dataPedido = dataPedido;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void adicionarItem(ItemPedido item) {
        item.setPedido(this);
        this.itens.add(item);
    }
}
