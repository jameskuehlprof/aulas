package br.com.sistemacad.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.sistemacad.conexao.Conexao;
import br.com.sistemacad.modelo.Cliente;

/**
 * DAO (Data Access Object) responsável por todo o acesso da tabela
 * "cliente" no banco de dados.
 *
 * Esta é a única classe do sistema que "conversa" diretamente com o
 * banco através de SQL. Servlets e JSPs nunca devem executar SQL
 * diretamente — eles sempre passam pelo DAO.
 *
 * IMPORTANTE: todos os métodos usam PreparedStatement (nunca
 * Statement/concatenação de String) exatamente para evitar SQL Injection.
 *
 * Exemplo do problema que o PreparedStatement evita:
 *
 *   // NUNCA FAÇA ISSO:
 *   String sql = "SELECT * FROM cliente WHERE nome = '" + nome + "'";
 *   // Um usuário mal-intencionado poderia digitar algo como:
 *   //   ' OR '1'='1
 *   // e alterar o comportamento da consulta.
 *
 * Com PreparedStatement, o valor digitado pelo usuário nunca é
 * interpretado como parte do comando SQL, apenas como dado.
 */
public class ClienteDAO {

    /**
     * Insere um novo cliente no banco de dados.
     */
    public void cadastrar(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO cliente (nome, email, cidade) VALUES (?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getEmail());
            stmt.setString(3, cliente.getCidade());

            stmt.executeUpdate();
        }
    }

    /**
     * Retorna todos os clientes cadastrados, ordenados por nome.
     */
    public List<Cliente> listar() throws SQLException {
        String sql = "SELECT id, nome, email, cidade FROM cliente ORDER BY nome";
        List<Cliente> clientes = new ArrayList<>();

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                clientes.add(mapearCliente(rs));
            }
        }

        return clientes;
    }

    /**
     * Busca um único cliente pelo id. Retorna null se não encontrar.
     * Utilizado principalmente para carregar os dados no formulário de edição.
     */
    public Cliente buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, nome, email, cidade FROM cliente WHERE id = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearCliente(rs);
                }
            }
        }

        return null;
    }

    /**
     * Busca clientes por cidade (desafio proposto na Aula 4).
     * Utiliza LIKE para permitir busca parcial (ex.: "Timb" encontra "Timbó").
     */
    public List<Cliente> buscarPorCidade(String cidade) throws SQLException {
        String sql = "SELECT id, nome, email, cidade FROM cliente "
                + "WHERE cidade LIKE ? ORDER BY nome";
        List<Cliente> clientes = new ArrayList<>();

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + cidade + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    clientes.add(mapearCliente(rs));
                }
            }
        }

        return clientes;
    }

    /**
     * Atualiza os dados de um cliente já existente (identificado pelo id).
     */
    public void alterar(Cliente cliente) throws SQLException {
        String sql = "UPDATE cliente SET nome = ?, email = ?, cidade = ? WHERE id = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getEmail());
            stmt.setString(3, cliente.getCidade());
            stmt.setInt(4, cliente.getId());

            stmt.executeUpdate();
        }
    }

    /**
     * Remove um cliente do banco de dados a partir do id.
     */
    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM cliente WHERE id = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    /**
     * Converte a linha atual do ResultSet em um objeto Cliente.
     */
    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setId(rs.getInt("id"));
        cliente.setNome(rs.getString("nome"));
        cliente.setEmail(rs.getString("email"));
        cliente.setCidade(rs.getString("cidade"));
        return cliente;
    }
}
