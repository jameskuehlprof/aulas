package br.com.exemplo.lojavirtual.dto;

import java.math.BigDecimal;

/**
 * DTO de saida do relatorio de vendas por categoria (exercicio da Aula 06).
 * Um objeto por categoria, ja com o total vendido calculado - quem consome
 * a API nao precisa saber que por tras existem Pedido, ItemPedido e Produto
 * sendo percorridos para chegar nesse numero.
 */
public class RelatorioDTO {

    private Long categoriaId;
    private String categoriaNome;
    private BigDecimal totalVendido;

    public RelatorioDTO() {
    }

    public RelatorioDTO(Long categoriaId, String categoriaNome, BigDecimal totalVendido) {
        this.categoriaId = categoriaId;
        this.categoriaNome = categoriaNome;
        this.totalVendido = totalVendido;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getCategoriaNome() {
        return categoriaNome;
    }

    public void setCategoriaNome(String categoriaNome) {
        this.categoriaNome = categoriaNome;
    }

    public BigDecimal getTotalVendido() {
        return totalVendido;
    }

    public void setTotalVendido(BigDecimal totalVendido) {
        this.totalVendido = totalVendido;
    }
}
