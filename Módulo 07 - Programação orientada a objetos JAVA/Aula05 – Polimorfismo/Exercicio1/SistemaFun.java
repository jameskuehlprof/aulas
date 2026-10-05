public class SistemaFun {
    public static void main(String[] args) {
        Gerente gerente = new Gerente("Matheus", 8000);
        Vendedor vendedor = new Vendedor("Ana", 4000);
        System.out.println("Bônus gerente: " +  gerente.calcularBonus());
        System.out.println("Bônus vendedor: " + vendedor.calcularBonus());
    }
}