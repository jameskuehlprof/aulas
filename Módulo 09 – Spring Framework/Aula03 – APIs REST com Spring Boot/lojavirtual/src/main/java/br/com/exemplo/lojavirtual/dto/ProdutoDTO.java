package br.com.exemplo.lojavirtual.dto;

import java.math.BigDecimal;

/**
 * Camada DTO (Data Transfer Object): é o formato que efetivamente trafega
 * pela API — o que o cliente da API (navegador, app, Postman) recebe.
 *
 * Por que não devolver a entidade "Produto" direto?
 *  - Para não expor detalhes internos do domínio (aqui é simples, mas em
 *    projetos reais o Model costuma ter campos sensíveis, relacionamentos
 *    e regras que não devem "vazar" para fora da API);
 *  - Para poder mudar o Model sem quebrar o contrato da API;
 *  - Para controlar exatamente o que é público.
 */

public class ProdutoDTO {

    private Long id;
    private String nome;
    private BigDecimal preco;

    public ProdutoDTO() {
    }

    public ProdutoDTO(Long id, String nome, BigDecimal preco) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
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
}
