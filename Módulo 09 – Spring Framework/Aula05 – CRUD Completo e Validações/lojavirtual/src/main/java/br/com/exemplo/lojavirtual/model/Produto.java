package br.com.exemplo.lojavirtual.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * A partir da Aula 4, Produto deixa de ser um POJO comum e passa a ser uma
 * ENTIDADE JPA: uma classe Java mapeada para uma tabela do banco de dados.
 *
 * @Entity   -> diz ao Hibernate que esta classe vira uma tabela.
 * @Table    -> nome explícito da tabela (opcional; sem ele, usaria "produto"
 *              mesmo, a partir do nome da classe).
 * @Id       -> marca o atributo que é a chave primária.
 * @GeneratedValue -> o próprio banco gera o valor do id (auto incremento).
 */
@Entity
@Table(name = "produto")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
