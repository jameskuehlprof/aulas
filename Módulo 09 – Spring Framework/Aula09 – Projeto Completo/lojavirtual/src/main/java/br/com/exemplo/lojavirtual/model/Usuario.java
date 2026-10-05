package br.com.exemplo.lojavirtual.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Representa um usuário que pode se autenticar na API (ex.: um funcionário
 * da loja que usa o sistema de administração). Não confundir com Cliente,
 * que é quem compra produtos e não precisa de login nesta versão do projeto.
 */
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    // Nunca armazenamos a senha em texto puro — sempre o hash gerado pelo BCrypt.
    private String senha;

    private boolean habilitado = true;

    public Usuario() {
    }

    public Usuario(String username, String senha) {
        this.username = username;
        this.senha = senha;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean isHabilitado() {
        return habilitado;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }
}
