import java.util.Scanner;

public class tabuada {

    public static void main(String[] args) {

        Scanner entrada = new Scanner(System.in);

        System.out.print("Digite um número para ver a tabuada: ");
        Double numero = entrada.nextDouble();

        System.out.println("\nTabuada do " + numero + ":");

        for (int i = 1; i <= 10; i++) {
            System.out.println(numero + " / " + i + " = " + (numero / i));
        }

        entrada.close();
    }
}