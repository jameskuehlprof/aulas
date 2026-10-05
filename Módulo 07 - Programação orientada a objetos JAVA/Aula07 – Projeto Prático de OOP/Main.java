public class Main {
    public static void main(String[] args) {
        Autor autor = new Autor("Robert Martin");
        Livro livro = new Livro("Clean Code", autor);
        Usuario usuario = new Usuario("Maria", 1001);
        Emprestimo emprestimo = new Emprestimo(livro, usuario);
        usuario.apresentar();
        emprestimo.realizar();
    }
}