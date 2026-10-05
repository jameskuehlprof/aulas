public class numeros {

    public static void main(String[] args) {
       
        System.out.println("Números pares de 1 a 10:");
        for (int i = 1; i <= 10; i++) {
            if (i % 2 == 0) {
                System.out.println(i);
            }
        }

        System.out.println("\nNúmeros ímpares de 1 a 10:");
        for (int i = 1; i <= 10; i++) {
            if (i % 2 != 0) {
                System.out.println(i);
            }
        }
        
        int soma = 0;
        for (int i = 1; i <= 10; i++) {
            soma += i;
        }

        System.out.println("\nSoma de 1 a 10: " + soma);
    }
}