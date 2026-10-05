package br.com.sistemacad.conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe responsável por centralizar a criação de conexões com o banco
 * de dados MySQL, evitando repetir os dados de acesso em todo o sistema.
 *
 * URL de conexão:
 *   jdbc:mysql://localhost:3306/sistemacad
 *              |         |         |
 *              |         |         └── nome do banco de dados
 *              |         └──────────── porta padrão do MySQL
 *              └────────────────────── host onde o MySQL está rodando
 *
 * Ajuste as constantes USUARIO e SENHA de acordo com a configuração
 * do MySQL na sua máquina (Workbench > Server Status ou serviço do MySQL
 * no Windows).
 *
 * NOTA SOBRE "No suitable driver found":
 * Desde o JDBC 4.0, o driver do MySQL se registra sozinho no DriverManager
 * automaticamente (via META-INF/services), sem precisar de Class.forName().
 * Isso funciona muito bem em uma aplicação Java comum. Porém, dentro de um
 * servidor de aplicação como o Tomcat — especialmente quando o Eclipse
 * publica/republica o projeto várias vezes durante o desenvolvimento — o
 * carregador de classes (ClassLoader) da aplicação web pode, em alguns
 * cenários, não repetir esse registro automático corretamente.
 * Por isso, carregamos o driver explicitamente no bloco estático abaixo:
 * é uma forma mais "defensiva" e didática, muito usada em aplicações web,
 * que garante o funcionamento mesmo nesses cenários.
 */
public class Conexao {

    private static final String URL =
            "jdbc:mysql://localhost:3306/sistemacad"
            + "?useTimezone=true&serverTimezone=America/Sao_Paulo"
            + "&useSSL=false&allowPublicKeyRetrieval=true";

    private static final String USUARIO = "root";
    private static final String SENHA = "admin";

    static {
        try {
            // Força o carregamento e registro do driver do MySQL.
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(
                    "Driver do MySQL (mysql-connector-j) não encontrado no classpath. "
                    + "Verifique a dependência no pom.xml e se o .jar está em WEB-INF/lib.");
        }
    }

    // Classe utilitária: não deve ser instanciada.
    private Conexao() {
    }

    /**
     * Abre e retorna uma nova conexão com o banco de dados sistemacad.
     * Recomenda-se sempre utilizar try-with-resources ao chamar este
     * método, para garantir o fechamento automático da conexão:
     *
     * try (Connection conn = Conexao.conectar()) {
     *     ...
     * }
     *
     * @return uma Connection aberta com o MySQL
     * @throws SQLException caso não seja possível conectar
     */
    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}
