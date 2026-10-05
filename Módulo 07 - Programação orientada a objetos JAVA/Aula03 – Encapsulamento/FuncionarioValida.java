public class FuncionarioValida {
    // Atributo privado
    private double salario;
    // Setter com validação
    public void setSalario(double salario) {
        if (salario > 0) {
            this.salario = salario;
        } else {
            System.out.println("Erro: O salário deve ser maior que zero.");
        }    }
    // Getter
    public double getSalario() {
        return salario;
    }
    public static void main(String[] args) {
        FuncionarioValida f = new FuncionarioValida();
        // Salário válido
        f.setSalario(4500.00);
        System.out.println("Salário: R$ " + f.getSalario());
        // Tentativa de salário inválido
        f.setSalario(-1200.00);
        System.out.println("Salário: R$ " + f.getSalario());
    }}