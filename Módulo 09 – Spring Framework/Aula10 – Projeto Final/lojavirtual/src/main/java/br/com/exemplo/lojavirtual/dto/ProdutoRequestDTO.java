package br.com.exemplo.lojavirtual.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class ProdutoRequestDTO {

    @NotBlank(message = "o nome é obrigatório")
    private String nome;

    @NotNull(message = "o preço é obrigatório")
    @Positive(message = "o preço deve ser maior que zero")
    private BigDecimal preco;

    @NotNull(message = "a categoria é obrigatória")
    private Long categoriaId;

    @NotNull(message = "o estoque mínimo é obrigatório")
    @PositiveOrZero(message = "o estoque mínimo não pode ser negativo")
    private Integer estoqueMinimo;

    public ProdutoRequestDTO() {
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

    public Integer getEstoqueMinimo() {
        return estoqueMinimo;
    }

    public void setEstoqueMinimo(Integer estoqueMinimo) {
        this.estoqueMinimo = estoqueMinimo;
    }
}
