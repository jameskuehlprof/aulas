public class calcular {
    public static void main(String[] args) {
        Pessoa p = new Pessoa();
        p.apresentar();
        double media = p.calcularMedia(8, 10);
        System.out.println("Média: " + media);
    }
}