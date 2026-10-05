package br.com.exemplo.lojavirtualinterface.modelo;

import java.math.BigDecimal;

/** Espelha br.com.exemplo.lojavirtual.dto.RelatorioDTO (API, exercício da Aula 6). */
public class RelatorioDTO {

    private Long categoriaId;
    private String categoriaNome;
    private BigDecimal totalVendido;

    public RelatorioDTO() {
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
