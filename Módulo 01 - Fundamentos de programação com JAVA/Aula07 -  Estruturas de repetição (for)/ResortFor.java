import java.util.Scanner;

public class ResortFor {

    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
       
        for (int i = 1; i <= 5; i++) {          

            System.out.print("Digite Idade: ");
            int idade = entrada.nextInt();

            System.out.print("Digite quantidade de diárias: ");
            int diarias = entrada.nextInt();

            double valorTotal;
            double valorDiaria;
            int categoria;
            String classificacao;
            String situacao;

            if (idade >= 0 && idade <= 10) {
                valorDiaria = 500.12;
                categoria = 1;
            } else if (idade >= 11 && idade <= 59) {
                valorDiaria = 1000.14;
                categoria = 2;
            } else if (idade >= 60) {
                valorDiaria = 700.25;
                categoria = 3;
            } else {
                valorDiaria = 0;
                categoria = 0;
            }
            switch (categoria) {
                case 1:
                    classificacao = "Criança";
                    break;
                case 2:
                    classificacao = "Adulto";
                    break;
                case 3:
                    classificacao = "Idoso";
                    break;
                default:
                    classificacao = "Desconhecido";
            }
            valorTotal = valorDiaria * diarias;
            if (valorTotal > 3000) {
                situacao = "Cliente fiel";
            } else {
                situacao = "Evoluir na fidelidade";
            }
            System.out.println("Idade: " + idade +
                    " | Categoria: " + categoria +
                    " | Classificação: " + classificacao +
                    " | Valor diárias: R$ " + valorTotal +
                    " | Situação: " + situacao);
        System.out.println("Posição: " + i);            
        }
        entrada.close();
    }
}