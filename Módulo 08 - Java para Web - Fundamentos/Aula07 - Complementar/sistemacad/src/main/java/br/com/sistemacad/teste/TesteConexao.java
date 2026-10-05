package br.com.sistemacad.teste;

import java.sql.Connection;

import br.com.sistemacad.conexao.Conexao;

/**
 * Classe simples para testar, de forma isolada (sem servidor web),
 * se a conexão Java <-> MySQL está funcionando corretamente.
 *
 * Como usar no Eclipse:
 *   Botão direito no arquivo -> Run As -> Java Application
 *
 * Esta classe NÃO faz parte do fluxo web da aplicação; ela existe
 * apenas como ferramenta de diagnóstico para o professor/aluno,
 * conforme praticado na Aula 4 (Exercício 1 - Primeira conexão).
 */
public class TesteConexao {

    public static void main(String[] args) {
        System.out.println("=================================");
        System.out.println("           SISTEMACAD");
        System.out.println("=================================");

        try (Connection conn = Conexao.conectar()) {
            if (conn.isValid(5)) {
                System.out.println("Conexão com MySQL estabelecida!");
                System.out.println("=================================");
                System.out.println("Banco:    sistemacad");
                System.out.println("Servidor: localhost");
                System.out.println("Porta:    3306");
                System.out.println("Status:   Conectado");
            }
        } catch (Exception e) {
            System.out.println("Erro ao conectar com o banco!");
            System.out.println("Verifique:");
            System.out.println(" 1) O serviço do MySQL está em execução?");
            System.out.println(" 2) O banco 'sistemacad' foi criado (sql/schema.sql)?");
            System.out.println(" 3) Usuário e senha em Conexao.java estão corretos?");
            e.printStackTrace();
        }

        System.out.println("=================================");
    }
}
