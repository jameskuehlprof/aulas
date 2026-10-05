public class Emprestimo {
    private Livro livro;
    private Usuario usuario;
    public Emprestimo(Livro livro, Usuario usuario){
        this.livro = livro;
        this.usuario = usuario;
    }
    public void realizar(){
        if(livro.isDisponivel()){
            livro.emprestar();
            System.out.println(
                usuario.getNome()
                + " emprestou "
                + livro.getTitulo());
        }else{
            System.out.println("Livro indisponível.");
        }
    }
}