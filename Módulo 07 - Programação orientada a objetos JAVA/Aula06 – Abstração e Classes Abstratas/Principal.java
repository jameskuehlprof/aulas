public class Principal {
    public static void main(String[] args) {
        Professor professor = new Professor("Carlos", 60, 160);
        Secretaria secretaria = new Secretaria("Maria", 3200);
        professor.apresentar();
        System.out.println(professor.calcularSalario());
        System.out.println();
        secretaria.apresentar();
        System.out.println(secretaria.calcularSalario());
    }
}