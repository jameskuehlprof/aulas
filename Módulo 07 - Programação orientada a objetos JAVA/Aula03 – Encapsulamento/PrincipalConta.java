public class PrincipalConta {
        public static void main(String[] args){
       Conta conta = new Conta();
        conta.depositar(500);
        conta.sacar(150);
        System.out.println(conta.getSaldo());
      }  }