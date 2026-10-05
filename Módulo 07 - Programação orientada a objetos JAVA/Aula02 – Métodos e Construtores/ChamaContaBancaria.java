public class ChamaContaBancaria {

    public static void main(String[] args) {

        // Criando uma conta bancária
        ContaBancaria conta = new ContaBancaria("João Silva", 1000.00);

        // Consultando saldo inicial
        System.out.println("=== Saldo Inicial ===");
        conta.consultarSaldo();

        // Depositando dinheiro
        System.out.println("\n=== Depósito ===");
        conta.depositar(500);

        // Consultando saldo
        conta.consultarSaldo();

        // Sacando dinheiro
        System.out.println("\n=== Saque ===");
        conta.sacar(300);

        // Consultando saldo
        conta.consultarSaldo();

        // Tentando sacar um valor maior que o saldo
        System.out.println("\n=== Tentativa de saque maior que o saldo ===");
        conta.sacar(2000);

        // Saldo final
        conta.consultarSaldo();
    }
}