public class Diretor extends Funcionario {
    private double salario;
    public Diretor(String nome, double salario) {
        super(nome);
        this.salario = salario;
    }
    @Override
    public double calcularSalario() {
        return salario * 1.8;
    }
}