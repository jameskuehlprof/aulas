package br.com.exemplo.lojavirtual.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoriaRequestDTO {

    @NotBlank(message = "o nome é obrigatório")
    @Size(min = 3, max = 50, message = "o nome deve ter entre 3 e 50 caracteres")
    private String nome;

    public CategoriaRequestDTO() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
