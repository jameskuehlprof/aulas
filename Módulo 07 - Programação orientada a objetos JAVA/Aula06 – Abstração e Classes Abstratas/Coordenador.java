public class Coordenador extends Funcionario {
    private double salario;
    public Coordenador(String nome, double salario) {
        super(nome);
        this.salario = salario;
    }
    @Override
    public double calcularSalario() {
        return salario * 1.9;
    }
}