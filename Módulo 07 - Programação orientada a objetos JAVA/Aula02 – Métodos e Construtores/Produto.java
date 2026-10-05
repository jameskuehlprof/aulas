public class Produto{
    String nome;
    double preco;
    public Produto(String nome,double preco){
        this.nome = nome;
        this.preco = preco;
    }
    void mostrar(){
        System.out.println(nome);
        System.out.println(preco);
    }
}
