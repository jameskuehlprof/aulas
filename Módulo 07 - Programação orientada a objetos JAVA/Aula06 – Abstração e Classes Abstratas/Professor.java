public class Professor extends Funcionario {
    private double valorHora;
    private int horas;
    public Professor(String nome, double valorHora, int horas) {
        super(nome);
        this.valorHora = valorHora;
        this.horas = horas;
    }
    @Override
    public double calcularSalario() {
        return valorHora * horas;
    }
}