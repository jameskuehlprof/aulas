package br.com.exemplo.lojavirtual.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO de ENTRADA: é o que o cliente da API envia no corpo do POST/PUT.
 * Separar o DTO de entrada do DTO de saída (ProdutoDTO) é uma boa prática:
 * o que eu recebo nem sempre tem os mesmos campos (nem as mesmas regras) do
 * que eu devolvo. Aqui, por exemplo, faz sentido validar nome e preço na
 * entrada — mas o id nunca vem do cliente (é gerado pelo banco).
 */
public class ProdutoRequestDTO {

    @NotBlank(message = "o nome é obrigatório")
    private String nome;

    @NotNull(message = "o preço é obrigatório")
    @Positive(message = "o preço deve ser maior que zero")
    private BigDecimal preco;

    public ProdutoRequestDTO() {
    }

    public ProdutoRequestDTO(String nome, BigDecimal preco) {
        this.nome = nome;
        this.preco = preco;
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
}
