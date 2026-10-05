public class Aluno extends Pessoa{
    private String curso;
    public Aluno(String nome,String curso){
        super(nome);
        this.curso = curso;}
    public void exibir(){
        System.out.println(nome);
        System.out.println(curso);
    }}