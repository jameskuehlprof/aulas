public class PessoaValida {
    // Atributo privado
    private int idade;
    // Setter com validação
    public void setIdade(int idade) {
        if (idade >= 0) {
            this.idade = idade;
        } else {
            System.out.println("Erro: A idade não pode ser negativa.");
        }
    }
    // Getter
    public int getIdade() {
        return idade;
    }
    public static void main(String[] args) {
        PessoaValida p = new PessoaValida();
        // Valor válido
        p.setIdade(25);
        System.out.println("Idade: " + p.getIdade());
        // Valor inválido
        p.setIdade(-8);
        System.out.println("Idade: " + p.getIdade());
    }
}