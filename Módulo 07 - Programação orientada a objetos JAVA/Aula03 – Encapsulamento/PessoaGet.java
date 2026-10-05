public class PessoaGet {
    // Atributo privado
    private String nome;
    // Construtor
    public PessoaGet(String nome) {
        this.nome = nome;
    }
    // Getter - retorna o valor do atributo nome
    public String getNome() {
        return nome;
    }
    public static void main(String[] args) {
        // Criando um objeto
        PessoaGet p = new PessoaGet("João");
        // Utilizando o getter para consultar o nome
        System.out.println("Nome da pessoa: " + p.getNome());
    }
}