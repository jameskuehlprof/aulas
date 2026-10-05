public class Funcionario {
    // Atributos
    String nome;
    double salario;
    // Construtor
    public Funcionario(String nome, double salario) {
        this.nome = nome;
        this.salario = salario;
    }
    // Método para aumentar o salário
    void aumentarSalario(double percentual) {
        salario += salario * percentual / 100;
    }
    // Método para apresentar os dados do funcionário
    void apresentar() {
        System.out.println("Nome: " + nome);
        System.out.println("Salário: R$ " + salario);
    }
}