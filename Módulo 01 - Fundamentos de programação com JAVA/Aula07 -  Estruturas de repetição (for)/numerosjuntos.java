public class numerosjuntos {

    public static void main(String[] args) {       
         int soma = 0;
        for (int i = 1; i <= 10; i++) {
            soma += i;
            if (i % 2 == 0) {
                System.out.println("Número par:" + i);                               
            } else {
                System.out.println("Número impar :" + i); 
            }
            System.out.println("\nSoma de 1 a 10: " + soma);
        }        
        
    }
}