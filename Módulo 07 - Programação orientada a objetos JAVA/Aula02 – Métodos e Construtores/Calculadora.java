
public class Calculadora {

    // Método para somar
    public double somar(double num1, double num2) {
        return num1 + num2;
    }

    // Método para subtrair
    public double subtrair(double num1, double num2) {
        return num1 - num2;
    }

    // Método para multiplicar
    public double multiplicar(double num1, double num2) {
        return num1 * num2;
    }

    // Método para dividir
    public double dividir(double num1, double num2) {
        if (num2 == 0) {
            System.out.println("Erro: divisão por zero não é permitida.");
            return 0;
        }
        return num1 / num2;
    }
}