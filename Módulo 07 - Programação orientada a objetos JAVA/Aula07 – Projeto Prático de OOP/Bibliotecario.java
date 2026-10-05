public class Bibliotecario extends Pessoa{
    public Bibliotecario(String nome){
        super(nome);
    }
    @Override
    public void apresentar(){
        System.out.println("Bibliotecário: " + getNome());
    }
}