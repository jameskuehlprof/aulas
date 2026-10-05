public class PessoaSet {
    // Atributo privado
    private String nome;
    // Construtor
    public PessoaSet() {
    }
    // Setter - altera o valor do atributo nome
    public void setNome(String nome) {
        this.nome = nome;
    }
    // Getter - retorna o valor do atributo nome
    public String getNome() {
        return nome;
    }
    public static void main(String[] args) {
        // Criando um objeto
        PessoaSet p = new PessoaSet();
        // Alterando o valor do atributo utilizando o setter
        p.setNome("Carlos");
        // Exibindo o valor atualizado
        System.out.println("Nome da pessoa: " + p.getNome());
    }
}