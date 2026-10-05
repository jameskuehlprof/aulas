public class Usuario extends Pessoa{
    private int matricula;
    public Usuario(String nome,int matricula){
        super(nome);
        this.matricula = matricula;
    }
    public int getMatricula(){
        return matricula;
    }
    @Override
    public void apresentar(){
        System.out.println("Usuário: " + getNome());
    }
}