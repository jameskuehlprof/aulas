import java.util.ArrayList; //comentario

public class TesteArrayList {
    public static void main(String[] args) {
        ArrayList<Integer> numeros = new ArrayList<>();
        numeros.add(4);
        numeros.add(5);

        System.out.println("Numeros");
        for (Integer numero : numeros) {
            System.out.println(numero);
        }

        System.out.println("Nomes");
        ArrayList<String> nomes = new ArrayList<>();
        nomes.add("Ana");
        nomes.add("Carlos");
        nomes.add("Maria");
        for (String nome : nomes) {
            System.out.println(nome);
        }
    }
}