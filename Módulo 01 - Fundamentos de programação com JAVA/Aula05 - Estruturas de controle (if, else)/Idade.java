import java.util.Scanner;

public class Idade {
    public static void main(String[] args) {

        Scanner entrada = new Scanner(System.in);

        System.out.print("Digite sua idade: ");
        int idade = entrada.nextInt();

        if (idade < 0) {
            System.out.println("Idade inválida");
        } else if (idade < 12) {
            System.out.println("Criança");
        } else if (idade >= 12 && idade <= 17) {
            System.out.println("Adolescente");
        } else if (idade >= 18 && idade <= 59) {
            System.out.println("Adulto");
        } else {
            System.out.println("Idoso");
        }

        entrada.close();
    }
}