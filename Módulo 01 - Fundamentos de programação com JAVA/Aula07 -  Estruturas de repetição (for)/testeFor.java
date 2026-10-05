public class testeFor {
    public static void main(String[] args) {
        int soma = 100;
        int i;
        for (i = 1; i <= 5; i++) {
            soma += i;
            System.out.println("Número: " + i);
        }
        System.out.println("Soma: " + soma);
    }
}