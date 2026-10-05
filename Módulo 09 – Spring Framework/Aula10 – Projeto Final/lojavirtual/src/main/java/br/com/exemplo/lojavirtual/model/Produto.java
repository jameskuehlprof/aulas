package br.com.exemplo.lojavirtual.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "produto")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private BigDecimal preco;

    private Integer quantidadeEmEstoque;

    /**
     * Estoque mínimo desejado para o produto. Usado pela regra de negócio
     * do endpoint /produtos/estoque-baixo: um produto está com estoque
     * baixo quando quantidadeEmEstoque < estoqueMinimo.
     */
    private Integer estoqueMinimo;

    /**
     * Lado "MUITOS" do relacionamento com Categoria: muitos produtos podem
     * pertencer à mesma categoria.
     *
     * fetch = LAZY: o Hibernate só busca a Categoria no banco quando
     * produto.getCategoria() for de fato chamado — evita carregar dados que
     * talvez nunca sejam usados. (O padrão do @ManyToOne é EAGER; aqui
     * escolhemos LAZY de propósito.)
     *
     * @JoinColumn define o nome da coluna de chave estrangeira na tabela
     * produto (categoria_id).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    public Produto() {
    }

    public Produto(Long id, String nome, BigDecimal preco, Integer quantidadeEmEstoque, Integer estoqueMinimo) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.quantidadeEmEstoque = quantidadeEmEstoque;
        this.estoqueMinimo = estoqueMinimo;
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

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Integer getQuantidadeEmEstoque() {
        return quantidadeEmEstoque;
    }

    public void setQuantidadeEmEstoque(Integer quantidadeEmEstoque) {
        this.quantidadeEmEstoque = quantidadeEmEstoque;
    }

    public Integer getEstoqueMinimo() {
        return estoqueMinimo;
    }

    public void setEstoqueMinimo(Integer estoqueMinimo) {
        this.estoqueMinimo = estoqueMinimo;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}
