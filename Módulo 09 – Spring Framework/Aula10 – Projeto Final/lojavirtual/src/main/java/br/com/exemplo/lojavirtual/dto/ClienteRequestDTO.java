package br.com.exemplo.lojavirtual.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ClienteRequestDTO {

    @NotBlank(message = "o nome é obrigatório")
    private String nome;

    @NotBlank(message = "o e-mail é obrigatório")
    @Email(message = "informe um e-mail válido")
    private String email;

    @NotBlank(message = "o CPF é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "o CPF deve conter exatamente 11 dígitos, sem pontos ou traço")
    private String cpf;

    public ClienteRequestDTO() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
