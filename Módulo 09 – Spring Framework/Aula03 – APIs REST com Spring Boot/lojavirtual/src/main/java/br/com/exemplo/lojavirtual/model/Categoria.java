package br.com.exemplo.lojavirtual.model;

/**
 * Camada MODEL: representa o "Categoria" dentro do domínio da loja virtual.
 * Nesta aula ainda não usamos banco de dados (isso vem na Aula 04 com o
 * Spring Data JPA) — por enquanto o Categoria é um POJO comum, guardado em
 * memória pelo CategoriaRepository.
 */
public class Categoria {

    private Long id;
    private String nome;

    public Categoria() {
    }

    public Categoria(Long id, String nome) {
        this.id = id;
        this.nome = nome;
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
