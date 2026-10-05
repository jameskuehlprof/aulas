import java.util.Scanner;

public class Resto {
    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
		System.out.print("Digite o primeiro número: ");
        double a = entrada.nextDouble();
		System.out.print("Digite o segundo número: ");
        double b = entrada.nextDouble();
        double Resultado = a % b;
        boolean comparacao = (Resultado <= 100);
        System.out.println ("Restou: " + Resultado);
        System.out.println ("Resto é menor que 100? " + comparacao);
        entrada.close();
    }
}