public class Livro {
    private String titulo;
    private Autor autor;
    private boolean disponivel = true;
    public Livro(String titulo, Autor autor){
        this.titulo = titulo;
        this.autor = autor;
    }
    public String getTitulo(){
        return titulo;
    }
    public Autor getAutor(){
        return autor;
    }
    public boolean isDisponivel(){
        return disponivel;
    }
    public void emprestar(){
        disponivel = false;
    }
    public void devolver(){
        disponivel = true;
    }
}