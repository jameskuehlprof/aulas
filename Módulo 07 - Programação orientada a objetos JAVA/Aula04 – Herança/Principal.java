public class Principal {
    public static void main(String[] args){
    Professor professor = new Professor();
    professor.nome = "João";
    professor.setSalario(8500);
    Aluno aluno = new Aluno("Carlos","Java");
    aluno.exibir();
    System.out.println(professor.nome);
    System.out.println(professor.getSalario());
}}