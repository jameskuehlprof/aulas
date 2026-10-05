public class ExemploArray {
    public static void main(String[] args) {

        int[] numeros = {10, 20, 30, 40, 50};

        // Acessando elementos
        System.out.println("Primeiro número: " + numeros[0]);
        System.out.println("Terceiro número: " + numeros[2]);

        // Percorrendo o array
        for (int i = 2; i < numeros.length; i++) {
            System.out.println("Posição " + i + ": " + numeros[i]);
        }
    }
}