public class ContaBancaria {

    // Atributos
    String titular;
    double saldo;

    // Construtor
    public ContaBancaria(String titular, double saldo) {
        this.titular = titular;
        this.saldo = saldo;
    }

    // Método para depositar dinheiro
    void depositar(double valor) {
        saldo += valor;
        System.out.println("Depósito de R$ " + valor + " realizado com sucesso!");
    }

    // Método para sacar dinheiro
    void sacar(double valor) {
        if (valor <= saldo) {
            saldo -= valor;
            System.out.println("Saque de R$ " + valor + " realizado com sucesso!");
        } else {
            System.out.println("Saldo insuficiente para realizar o saque.");
        }
    }

    // Método para consultar o saldo
    void consultarSaldo() {
        System.out.println("Titular: " + titular);
        System.out.println("Saldo atual: R$ " + saldo);
    }
}