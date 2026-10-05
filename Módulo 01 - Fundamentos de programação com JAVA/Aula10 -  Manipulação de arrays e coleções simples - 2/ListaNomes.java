import java.util.ArrayList;
import java.util.Scanner;
public class ListaNomes {
    public static void main(String[] args) {

        Scanner entrada = new Scanner(System.in);
        ArrayList<String> nomes = new ArrayList<>();

        int opcao;

        do {
            System.out.println("\n===== MENU =====");
            System.out.println("1 - Adicionar nome");
            System.out.println("2 - Remover nome");
            System.out.println("3 - Listar nomes");
            System.out.println("4 - Sair");
            System.out.print("Escolha uma opção: ");

            opcao = entrada.nextInt();
            entrada.nextLine(); // limpar buffer

            switch (opcao) {

                case 1:
                    System.out.print("Digite o nome para adicionar: ");
                    String nomeAdicionar = entrada.nextLine();
                    nomes.add(nomeAdicionar);
                    System.out.println("Nome adicionado com sucesso!");
                    break;

                case 2:
                    System.out.print("Digite o nome para remover: ");
                    String nomeRemover = entrada.nextLine();

                    if (nomes.remove(nomeRemover)) {
                        System.out.println("Nome removido com sucesso!");
                    } else {
                        System.out.println("Nome não encontrado na lista.");
                    }
                    break;

                case 3:
                    System.out.println("\nLista de nomes:");
                    if (nomes.isEmpty()) {
                        System.out.println("Lista vazia.");
                    } else {
                        for (int i = 0; i < nomes.size(); i++) {
                            System.out.println((i + 1) + " - " + nomes.get(i));
                        }
                    }
                    break;

                case 4:
                    System.out.println("Encerrando o programa...");
                    break;

                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 4);
        entrada.close();
    }
}