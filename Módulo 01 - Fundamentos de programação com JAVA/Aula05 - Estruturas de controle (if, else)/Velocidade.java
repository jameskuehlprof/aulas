public class Velocidade {
    public static void main(String[] args) {

        // Velocidades dos automóveis
        double v1 = 85, v2 = 110, v3 = 115;

        // Cálculo da velocidade média
        double media = (v1 + v2 + v3) / 3;

        // Exibindo a média
        System.out.println("Velocidade média: " + media + " km/h");

        // Estrutura condicional
        if (media > 100) {
            System.out.println("Velocidade acima da permitida");
        } else {
            System.out.println("Velocidade está no limite");
        }
    }
}