import java.util.Scanner;

public class Multiplicador {
    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
		System.out.print("Digite o primeiro número: ");
        int a = entrada.nextInt();
		System.out.print("Digite o segundo número: ");
        int b = entrada.nextInt();
        System.out.println("Resultado: " + (a * b));
    }
}
