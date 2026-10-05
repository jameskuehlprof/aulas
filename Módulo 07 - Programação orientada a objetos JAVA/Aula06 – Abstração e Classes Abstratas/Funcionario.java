public abstract class Funcionario {
    protected String nome;
    public Funcionario(String nome) {
        this.nome = nome;
    }
    public void apresentar() {
        System.out.println("Funcionário: " + nome);
    }
    public abstract double calcularSalario();
}