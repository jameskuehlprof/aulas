package br.com.sistemacad.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.sistemacad.conexao.Conexao;
import br.com.sistemacad.modelo.ItemPedido;
import br.com.sistemacad.modelo.Pedido;

/**
 * DAO responsável pelo acesso às tabelas "pedido" e "item_pedido".
 *
 * Diferente do ClienteDAO (Aula 4), aqui um único "cadastrar" ou "alterar"
 * grava em DUAS tabelas relacionadas (pedido e item_pedido). Por isso
 * usamos uma TRANSAÇÃO: desligamos o autocommit, executamos todos os
 * comandos e só então confirmamos (commit). Se qualquer comando falhar,
 * desfazemos tudo (rollback) — o pedido nunca fica "pela metade" no banco.
 *
 *   conn.setAutoCommit(false);
 *   try {
 *       ... vários INSERT/UPDATE/DELETE ...
 *       conn.commit();
 *   } catch (SQLException e) {
 *       conn.rollback();
 *       throw e;
 *   }
 *
 * Assim como no ClienteDAO, todas as operações usam PreparedStatement
 * para evitar SQL Injection.
 */
public class PedidoDAO {

    /**
     * Insere um novo pedido e todos os seus itens, em uma única transação.
     */
    public void cadastrar(Pedido pedido) throws SQLException {
        String sqlPedido = "INSERT INTO pedido (cliente_id, data_pedido, status, valor_total) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar()) {
            conn.setAutoCommit(false);

            try {
                int pedidoId;

                try (PreparedStatement stmt =
                        conn.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {

                    stmt.setInt(1, pedido.getClienteId());
                    stmt.setObject(2, pedido.getDataPedido());
                    stmt.setString(3, pedido.getStatus());
                    stmt.setDouble(4, pedido.calcularValorTotal());
                    stmt.executeUpdate();

                    try (ResultSet chaves = stmt.getGeneratedKeys()) {
                        if (!chaves.next()) {
                            throw new SQLException("Não foi possível obter o id gerado para o pedido.");
                        }
                        pedidoId = chaves.getInt(1);
                    }
                }

                inserirItens(conn, pedidoId, pedido.getItens());

                conn.commit();
                pedido.setId(pedidoId);
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    /**
     * Atualiza os dados do pedido e substitui todos os seus itens
     * (exclui os itens antigos e insere os novos), também em transação.
     */
    public void alterar(Pedido pedido) throws SQLException {
        String sqlPedido = "UPDATE pedido SET cliente_id = ?, data_pedido = ?, status = ?, "
                + "valor_total = ? WHERE id = ?";
        String sqlExcluirItens = "DELETE FROM item_pedido WHERE pedido_id = ?";

        try (Connection conn = Conexao.conectar()) {
            conn.setAutoCommit(false);

            try {
                try (PreparedStatement stmt = conn.prepareStatement(sqlPedido)) {
                    stmt.setInt(1, pedido.getClienteId());
                    stmt.setObject(2, pedido.getDataPedido());
                    stmt.setString(3, pedido.getStatus());
                    stmt.setDouble(4, pedido.calcularValorTotal());
                    stmt.setInt(5, pedido.getId());
                    stmt.executeUpdate();
                }

                try (PreparedStatement stmt = conn.prepareStatement(sqlExcluirItens)) {
                    stmt.setInt(1, pedido.getId());
                    stmt.executeUpdate();
                }

                inserirItens(conn, pedido.getId(), pedido.getItens());

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    /**
     * Remove um pedido e todos os seus itens (nessa ordem, por causa da
     * chave estrangeira item_pedido.pedido_id -> pedido.id).
     */
    public void excluir(int id) throws SQLException {
        String sqlItens = "DELETE FROM item_pedido WHERE pedido_id = ?";
        String sqlPedido = "DELETE FROM pedido WHERE id = ?";

        try (Connection conn = Conexao.conectar()) {
            conn.setAutoCommit(false);

            try {
                try (PreparedStatement stmt = conn.prepareStatement(sqlItens)) {
                    stmt.setInt(1, id);
                    stmt.executeUpdate();
                }

                try (PreparedStatement stmt = conn.prepareStatement(sqlPedido)) {
                    stmt.setInt(1, id);
                    stmt.executeUpdate();
                }

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    /**
     * Lista todos os pedidos, com o nome do cliente já resolvido via JOIN.
     * Não carrega os itens (evita SELECTs desnecessários na listagem).
     */
    public List<Pedido> listar() throws SQLException {
        return listar(0);
    }

    /**
     * Lista apenas os pedidos de um cliente específico — usado no link
     * "Pedidos" da listagem de clientes (tudo fica vinculado por cliente_id).
     */
    public List<Pedido> listarPorCliente(int clienteId) throws SQLException {
        return listar(clienteId);
    }

    private List<Pedido> listar(int clienteId) throws SQLException {
        StringBuilder sql = new StringBuilder(
                "SELECT p.id, p.cliente_id, c.nome AS cliente_nome, p.data_pedido, "
                        + "p.status, p.valor_total "
                        + "FROM pedido p "
                        + "JOIN cliente c ON c.id = p.cliente_id ");

        if (clienteId > 0) {
            sql.append("WHERE p.cliente_id = ? ");
        }
        sql.append("ORDER BY p.data_pedido DESC, p.id DESC");

        List<Pedido> pedidos = new ArrayList<>();

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            if (clienteId > 0) {
                stmt.setInt(1, clienteId);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    pedidos.add(mapearPedido(rs));
                }
            }
        }

        return pedidos;
    }

    /**
     * Busca um pedido pelo id, já com o nome do cliente (JOIN) e a lista
     * completa de itens carregada. Usado para exibir o formulário de edição.
     */
    public Pedido buscarPorId(int id) throws SQLException {
        String sql = "SELECT p.id, p.cliente_id, c.nome AS cliente_nome, p.data_pedido, "
                + "p.status, p.valor_total "
                + "FROM pedido p JOIN cliente c ON c.id = p.cliente_id "
                + "WHERE p.id = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                Pedido pedido = mapearPedido(rs);
                pedido.setItens(buscarItens(conn, id));
                return pedido;
            }
        }
    }

    private List<ItemPedido> buscarItens(Connection conn, int pedidoId) throws SQLException {
        String sql = "SELECT id, pedido_id, produto, quantidade, preco_unitario "
                + "FROM item_pedido WHERE pedido_id = ? ORDER BY id";

        List<ItemPedido> itens = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pedidoId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ItemPedido item = new ItemPedido();
                    item.setId(rs.getInt("id"));
                    item.setPedidoId(rs.getInt("pedido_id"));
                    item.setProduto(rs.getString("produto"));
                    item.setQuantidade(rs.getInt("quantidade"));
                    item.setPrecoUnitario(rs.getDouble("preco_unitario"));
                    itens.add(item);
                }
            }
        }

        return itens;
    }

    /**
     * Insere, em lote (batch), os itens de um pedido já existente.
     * Chamado tanto por cadastrar() quanto por alterar(), sempre dentro
     * da mesma conexão/transação do método chamador (por isso recebe a
     * Connection já aberta, em vez de abrir uma nova).
     */
    private void inserirItens(Connection conn, int pedidoId, List<ItemPedido> itens)
            throws SQLException {

        String sql = "INSERT INTO item_pedido (pedido_id, produto, quantidade, preco_unitario) "
                + "VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (ItemPedido item : itens) {
                stmt.setInt(1, pedidoId);
                stmt.setString(2, item.getProduto());
                stmt.setInt(3, item.getQuantidade());
                stmt.setDouble(4, item.getPrecoUnitario());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    private Pedido mapearPedido(ResultSet rs) throws SQLException {
        Pedido pedido = new Pedido();
        pedido.setId(rs.getInt("id"));
        pedido.setClienteId(rs.getInt("cliente_id"));
        pedido.setClienteNome(rs.getString("cliente_nome"));
        pedido.setDataPedido(rs.getObject("data_pedido", LocalDate.class));
        pedido.setStatus(rs.getString("status"));
        pedido.setValorTotal(rs.getDouble("valor_total"));
        return pedido;
    }
}
