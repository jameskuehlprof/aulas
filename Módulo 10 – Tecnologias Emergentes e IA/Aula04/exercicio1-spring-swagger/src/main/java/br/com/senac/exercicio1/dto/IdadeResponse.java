package br.com.senac.exercicio1.dto;

/**
 * DTO (Data Transfer Object) devolvido pelo endpoint REST.
 *
 * Guarda tanto o resultado "util" (nome + idade prevista) quanto os dados
 * brutos da chamada feita para a API publica (status HTTP e corpo bruto),
 * exatamente como o enunciado do Exercicio 1 pede para exibir no console -
 * aqui eles tambem aparecem na resposta JSON, para dar pra ver pelo Swagger.
 */
public class IdadeResponse {

    private String nome;
    private Integer age;
    private int statusHttpOrigem;
    private String corpoBrutoOrigem;

    public IdadeResponse() {
        // construtor vazio exigido pelo Jackson para desserializacao
    }

    public IdadeResponse(String nome, Integer age, int statusHttpOrigem, String corpoBrutoOrigem) {
        this.nome = nome;
        this.age = age;
        this.statusHttpOrigem = statusHttpOrigem;
        this.corpoBrutoOrigem = corpoBrutoOrigem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public int getStatusHttpOrigem() {
        return statusHttpOrigem;
    }

    public void setStatusHttpOrigem(int statusHttpOrigem) {
        this.statusHttpOrigem = statusHttpOrigem;
    }

    public String getCorpoBrutoOrigem() {
        return corpoBrutoOrigem;
    }

    public void setCorpoBrutoOrigem(String corpoBrutoOrigem) {
        this.corpoBrutoOrigem = corpoBrutoOrigem;
    }
}
