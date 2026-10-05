public class Secretaria extends Funcionario {
    private double salario;
    public Secretaria(String nome, double salario) {
        super(nome);
        this.salario = salario;
    }
    @Override
    public double calcularSalario() {
        return salario;
    }
}