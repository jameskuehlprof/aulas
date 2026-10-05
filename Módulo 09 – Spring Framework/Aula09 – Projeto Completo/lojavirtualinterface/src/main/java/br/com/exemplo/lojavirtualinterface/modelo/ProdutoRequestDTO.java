package br.com.exemplo.lojavirtualinterface.modelo;

import java.math.BigDecimal;

/** Espelha br.com.exemplo.lojavirtual.dto.ProdutoRequestDTO (API). */
public class ProdutoRequestDTO {

    private String nome;
    private BigDecimal preco;
    private Long categoriaId;

    public ProdutoRequestDTO() {
    }

    public ProdutoRequestDTO(String nome, BigDecimal preco, Long categoriaId) {
        this.nome = nome;
        this.preco = preco;
        this.categoriaId = categoriaId;
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

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }
}
