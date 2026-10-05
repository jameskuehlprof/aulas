import java.util.Scanner;

public class MediaNotas {
    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
		System.out.print("Digite a primeira nota: ");
        double a = entrada.nextDouble();
		System.out.print("Digite a segunda nota: ");
        double b = entrada.nextDouble();      
        System.out.println("Media: " + (a + b) /2);
        entrada.close();
    }
}
