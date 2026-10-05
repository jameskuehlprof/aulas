import java.util.Scanner;
public class ArrayUsuario {
    public static void main(String[] args) {

        Scanner entrada = new Scanner(System.in);
        int[] numeros = new int[5];
       // int i2 = 0;
        // Entrada de dados
        for (int i = 0; i < numeros.length; i++) {
        //for (int numero : numeros) {
            System.out.print("Digite um número: ");
            numeros[i] = entrada.nextInt();
            //i2++;
        }

        // Exibindo os números
        System.out.println("Números digitados:");
        for (int i = 0; i < numeros.length; i++) {
            System.out.println(numeros[i]);
        }
       // System.out.println(numeros[2]);
        entrada.close();
    }
}