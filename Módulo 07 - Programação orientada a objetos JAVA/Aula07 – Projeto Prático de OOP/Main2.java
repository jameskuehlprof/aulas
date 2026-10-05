import java.util.ArrayList;
import java.util.Scanner;

public class Main2 {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        ArrayList<Livro> livros = new ArrayList<>();
        ArrayList<Usuario> usuarios = new ArrayList<>();

        int opcao;

        do {

            System.out.println("\n========= BIBLIOTECA =========");
            System.out.println("1 - Cadastrar Livro");
            System.out.println("2 - Cadastrar Usuário");
            System.out.println("3 - Realizar Empréstimo");
            System.out.println("4 - Devolver Livro");
            System.out.println("5 - Listar Livros");
            System.out.println("6 - Listar Usuários");
            System.out.println("7 - Sair");
            System.out.print("Escolha uma opção: ");

            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {

                case 1:

                    System.out.print("Título do livro: ");
                    String titulo = sc.nextLine();

                    System.out.print("Autor: ");
                    String nomeAutor = sc.nextLine();

                    Autor autor = new Autor(nomeAutor);
                    Livro livro = new Livro(titulo, autor);

                    livros.add(livro);

                    System.out.println("Livro cadastrado com sucesso!");

                    break;

                case 2:

                    System.out.print("Nome do usuário: ");
                    String nome = sc.nextLine();

                    System.out.print("Matrícula: ");
                    int matricula = sc.nextInt();
                    sc.nextLine();

                    Usuario usuario = new Usuario(nome, matricula);

                    usuarios.add(usuario);

                    System.out.println("Usuário cadastrado com sucesso!");

                    break;

                case 3:

                    if (livros.isEmpty() || usuarios.isEmpty()) {
                        System.out.println("Cadastre livros e usuários primeiro.");
                        break;
                    }

                    System.out.println("\nLivros:");

                    for (int i = 0; i < livros.size(); i++) {
                        System.out.println(i + " - " + livros.get(i).getTitulo());
                    }

                    System.out.print("Escolha o livro: ");
                    int livroEscolhido = sc.nextInt();

                    System.out.println("\nUsuários:");

                    for (int i = 0; i < usuarios.size(); i++) {
                        System.out.println(i + " - " + usuarios.get(i).getNome());
                    }

                    System.out.print("Escolha o usuário: ");
                    int usuarioEscolhido = sc.nextInt();
                    sc.nextLine();

                    Emprestimo emp = new Emprestimo(
                            livros.get(livroEscolhido),
                            usuarios.get(usuarioEscolhido));

                    emp.realizar();

                    break;

                case 4:

                    if (livros.isEmpty()) {
                        System.out.println("Nenhum livro cadastrado.");
                        break;
                    }

                    System.out.println("\nLivros:");

                    for (int i = 0; i < livros.size(); i++) {
                        System.out.println(i + " - " + livros.get(i).getTitulo());
                    }

                    System.out.print("Escolha o livro para devolver: ");
                    int devolver = sc.nextInt();
                    sc.nextLine();

                    livros.get(devolver).devolver();

                    System.out.println("Livro devolvido com sucesso!");

                    break;

                case 5:

                    System.out.println("\n===== LIVROS =====");

                    if (livros.isEmpty()) {
                        System.out.println("Nenhum livro cadastrado.");
                    } else {

                        for (Livro l : livros) {

                            System.out.println("----------------------------");
                            System.out.println("Título: " + l.getTitulo());
                            System.out.println("Autor : " + l.getAutor().getNome());
                            System.out.println("Status: "
                                    + (l.isDisponivel()
                                    ? "Disponível"
                                    : "Emprestado"));
                        }
                    }

                    break;

                case 6:

                    System.out.println("\n===== USUÁRIOS =====");

                    if (usuarios.isEmpty()) {
                        System.out.println("Nenhum usuário cadastrado.");
                    } else {

                        for (Usuario u : usuarios) {
                            System.out.println("----------------------------");
                            System.out.println("Nome: " + u.getNome());
                            System.out.println("Matrícula: " + u.getMatricula());
                        }
                    }

                    break;

                case 7:

                    System.out.println("Sistema encerrado.");

                    break;

                default:

                    System.out.println("Opção inválida.");

            }

        } while (opcao != 7);

        sc.close();
    }
}