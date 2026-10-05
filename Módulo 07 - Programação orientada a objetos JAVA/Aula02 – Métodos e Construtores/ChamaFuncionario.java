public class ChamaFuncionario {
    public static void main(String[] args) {
        // Criando um objeto da classe Funcionario
        Funcionario funcionario = new Funcionario("Maria", 3500.00);
        System.out.println("=== Dados Iniciais ===");
        funcionario.apresentar();
        // Aumentando o salário em 10%
        funcionario.aumentarSalario(10);
        System.out.println("\n=== Após aumento de 10% ===");
        funcionario.apresentar();
    }
}