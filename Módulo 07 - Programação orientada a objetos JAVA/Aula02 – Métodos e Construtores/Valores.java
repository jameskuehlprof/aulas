public class Valores {    
    public double calcularArea(double base, double altura) {
        return base * altura;
    }
    public double calcularPerimetro(double base, double altura) {
        return 2 * (base + altura);
    }
    public double calcularDesconto(double valor, double percentualDesconto) {
        return valor - (valor * percentualDesconto / 100);
    }
}