import java.util.Scanner;

public class Principal {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String nome;
        int idade;
        System.out.print("Nome: ");
        nome = sc.nextLine();
        System.out.print("Idade: ");
        idade = sc.nextInt();
        System.out.println(nome);
        System.out.println(idade);
    }
}
