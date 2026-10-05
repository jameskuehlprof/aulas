package br.com.exemplo.lojavirtual.dto;

/**
 * Repare que este DTO NÃO tem uma lista de produtos. Se devolvêssemos
 * Categoria -> List<Produto> -> cada Produto com sua Categoria -> ...
 * teríamos uma referência circular e um JSON gigante (ou um erro de
 * serialização). O DTO é exatamente o remédio para esse tipo de problema.
 */
public class CategoriaDTO {

    private Long id;
    private String nome;

    public CategoriaDTO() {
    }

    public CategoriaDTO(Long id, String nome) {
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
