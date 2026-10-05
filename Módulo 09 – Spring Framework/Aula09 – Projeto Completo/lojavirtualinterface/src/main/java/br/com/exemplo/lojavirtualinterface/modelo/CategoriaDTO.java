package br.com.exemplo.lojavirtualinterface.modelo;

/** Espelha br.com.exemplo.lojavirtual.dto.CategoriaDTO (API). */
public class CategoriaDTO {

    private Long id;
    private String nome;

    public CategoriaDTO() {
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
}
