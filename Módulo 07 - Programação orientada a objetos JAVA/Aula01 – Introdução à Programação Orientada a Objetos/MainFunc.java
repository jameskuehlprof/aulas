public class MainFunc {
    public static void main(String[] args) {
        Funcionario f1 = new Funcionario();
        f1.nome = "James";
        f1.cargo = "Professor";
        f1.salario = 1000;                
        System.out.println("======== Funcionário 1 ========");
        System.out.println("Nome: " + f1.nome);
        System.out.println("Cargo: " + f1.cargo);
        System.out.println("Salário: " + f1.salario);
        System.out.println();
        
    }
}