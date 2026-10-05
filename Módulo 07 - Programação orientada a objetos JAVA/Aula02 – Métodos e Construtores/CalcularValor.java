public class CalcularValor {
    public static void main(String[] args) {
        Valores p = new Valores();
        double area = p.calcularArea(10, 5);
        double perimetro = p.calcularPerimetro(10, 5);
        double valorFinal = p.calcularDesconto(250, 15);
        System.out.println("Área: " + area);
        System.out.println("Perímetro: " + perimetro);
        System.out.println("Valor com desconto: R$ " + valorFinal);
    }
}