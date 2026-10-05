public class Conta{
    private double saldo;
    public void depositar(double valor){
        if(valor > 0){
            saldo += valor;
        }
    }
    public void sacar(double valor){
        if(valor <= saldo){
            saldo -= valor;
        }
    }
    public double getSaldo(){
        return saldo;
        }
}