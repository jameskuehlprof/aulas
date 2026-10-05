import java.util.Scanner;
public class Assembleia {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int voto;
        int sim = 0;
        int nao = 0;
        int abstencao = 0;

        System.out.println("=== Sistema de Votação da Assembleia ===");

        do {
            System.out.println("\nEscolha seu voto:");
            System.out.println("1 - Sim");
            System.out.println("2 - Não");
            System.out.println("3 - Abstenção");
            System.out.println("0 - Encerrar votação");

            System.out.print("Digite seu voto: ");
            voto = scanner.nextInt();

            switch (voto) {
                case 1:
                    sim++;
                    break;
                case 2:
                    nao++;
                    break;
                case 3:
                    abstencao++;
                    break;
                case 0:
                    System.out.println("Encerrando votação...");
                    break;
                default:
                    System.out.println("Voto inválido!");
            }

        } while (voto != 0);

        // Resultado final
        int total = sim + nao + abstencao;

        System.out.println("\n=== Resultado da Assembleia ===");
        System.out.println("Total de votos: " + total);
        System.out.println("Sim: " + sim);
        System.out.println("Não: " + nao);
        System.out.println("Abstenção: " + abstencao);

        if (sim > nao) {
            System.out.println("Resultado: Proposta APROVADA");
        } else if (nao > sim) {
            System.out.println("Resultado: Proposta REPROVADA");
        } else {
            System.out.println("Resultado: EMPATE");
        }

        scanner.close();
    }
}