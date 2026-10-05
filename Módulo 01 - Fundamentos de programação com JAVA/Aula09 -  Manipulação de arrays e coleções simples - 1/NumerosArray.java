import java.util.Scanner;

public class NumerosArray {
    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
        int[] numeros = new int[10];
        int somaPar = 0;
        int somaImpar = 0;

        // Entrada de dados
        for (int i = 0; i < numeros.length; i++) {
            System.out.print("Digite um número: ");
            numeros[i] = entrada.nextInt();
            
        }
        System.out.println("Números pares do array:");
        for (int i = 0; i < numeros.length; i++) {
            if (numeros[i] % 2 == 0) {
                somaPar = somaPar +  numeros[i];
                System.out.println(numeros[i]);
            }
        }

        System.out.println("Números ímpares do array:");
        for (int i = 0; i < numeros.length; i++) {
            if (numeros[i] % 2 != 0) {
                somaImpar = somaImpar +  numeros[i];
                System.out.println(numeros[i]);
            }
        }
        System.out.println("Soma pares do array: " + somaPar);
        System.out.println("Soma ímpares do array: " + somaImpar);


        entrada.close();
    }
}