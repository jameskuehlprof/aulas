public class Main {
    public static void main(String[] args) {
        Calculadora calc = new Calculadora();
        double numero1 = 20;
        double numero2 = 5;
        System.out.println("Soma: " + calc.somar(numero1, numero2));
        System.out.println("Subtração: " + calc.subtrair(numero1, numero2));
        System.out.println("Multiplicação: " + calc.multiplicar(numero1, numero2));
        System.out.println("Divisão: " + calc.dividir(numero1, numero2));
    }
}