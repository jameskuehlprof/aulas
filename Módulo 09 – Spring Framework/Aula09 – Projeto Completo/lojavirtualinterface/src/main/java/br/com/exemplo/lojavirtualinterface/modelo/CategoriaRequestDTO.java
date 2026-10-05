package br.com.exemplo.lojavirtualinterface.modelo;

/** Espelha br.com.exemplo.lojavirtual.dto.CategoriaRequestDTO (API). */
public class CategoriaRequestDTO {

    private String nome;

    public CategoriaRequestDTO() {
    }

    public CategoriaRequestDTO(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
