public class Retorno {
    public static void main(String[] args) {
        Pessoa p = new Pessoa();
        double media = p.calcularMedia(8, 10);
        System.out.println("Média: " + media);
        
        int resultado = p.somar(10,20);
        System.out.println(resultado);
    }
}