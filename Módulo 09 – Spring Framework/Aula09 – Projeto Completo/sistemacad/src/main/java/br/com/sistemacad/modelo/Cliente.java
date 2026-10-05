package br.com.sistemacad.modelo;

/**
 * Representa um cliente cadastrado no sistema.
 * Classe simples (POJO), sem nenhuma regra de acesso a banco de dados, responsabilidade que fica a cargo de ClienteDAO.
 */
public class Cliente {

    private int id;
    private String nome;
    private String email;
    private String cidade;

    public Cliente() {
    }

    public Cliente(int id, String nome, String email, String cidade) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.cidade = cidade;
    }

    public Cliente(String nome, String email, String cidade) {
        this.nome = nome;
        this.email = email;
        this.cidade = cidade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    @Override
    public String toString() {
        return "Cliente{id=" + id + ", nome='" + nome + "', email='" + email
                + "', cidade='" + cidade + "'}";
    }
}
