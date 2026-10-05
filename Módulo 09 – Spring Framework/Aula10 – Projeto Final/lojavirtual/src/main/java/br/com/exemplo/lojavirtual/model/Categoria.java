package br.com.exemplo.lojavirtual.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Lado "UM" do relacionamento 1:N com Produto.
 *
 * mappedBy = "categoria" aponta para o atributo "categoria" lá em Produto —
 * é ELE quem tem a coluna de chave estrangeira (categoria_id). Aqui do lado
 * de Categoria não existe coluna nenhuma; é só uma "visão" da relação.
 *
 * Sem cascade e sem orphanRemoval de propósito: excluir uma Categoria NÃO
 * deve excluir os Produtos que pertencem a ela (ao contrário de Pedido/
 * ItemPedido, mais abaixo, onde o cascade faz todo sentido).
 */
@Entity
@Table(name = "categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @OneToMany(mappedBy = "categoria", cascade = {})
    private List<Produto> produtos = new ArrayList<>();

    public Categoria() {
    }

    public Categoria(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }
}
