package br.com.exemplo.lojavirtual.model;

import java.math.BigDecimal;

/**
 * Camada MODEL: representa o "Produto" dentro do domínio da loja virtual.
 * Nesta aula ainda não usamos banco de dados (isso vem na Aula 04 com o
 * Spring Data JPA) — por enquanto o Produto é um POJO comum, guardado em
 * memória pelo ProdutoRepository.
 */
public class Produto {

    private Long id;
    private String nome;
    private BigDecimal preco;
    private Integer quantidadeEmEstoque;

    public Produto() {
    }

    public Produto(Long id, String nome, BigDecimal preco, Integer quantidadeEmEstoque) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.quantidadeEmEstoque = quantidadeEmEstoque;
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
}
