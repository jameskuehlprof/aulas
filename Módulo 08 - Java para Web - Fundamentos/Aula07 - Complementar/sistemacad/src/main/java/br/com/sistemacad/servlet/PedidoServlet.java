package br.com.sistemacad.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import br.com.sistemacad.dao.ClienteDAO;
import br.com.sistemacad.dao.PedidoDAO;
import br.com.sistemacad.modelo.ItemPedido;
import br.com.sistemacad.modelo.Pedido;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet responsável pela camada web do CRUD de Pedido / ItemPedido
 * (Aula 07 — complementar à Aula 4, que tratou apenas de Cliente).
 *
 * Fluxo da aplicação (o mesmo padrão do ClienteServlet):
 *
 *   Navegador (JSP, com Bootstrap)
 *        |
 *        v
 *   PedidoServlet   <-- você está aqui
 *        |
 *        v
 *   PedidoDAO  (grava pedido + itens em uma transação)
 *        |
 *        v
 *   PreparedStatement / Connection / Driver JDBC
 *        |
 *        v
 *   MySQL (tabelas pedido e item_pedido, ligadas por FK)
 *
 * O parâmetro "acao" define a operação, exatamente como em ClienteServlet.
 * Além disso, esta tela pode receber um parâmetro opcional "clienteId"
 * (vindo do botão "Pedidos" na listagem de clientes) para filtrar os
 * pedidos de um único cliente — essa é a "ligação" entre as duas telas.
 */
@WebServlet("/pedidos")
public class PedidoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String[] STATUS_VALIDOS = {
        Pedido.STATUS_ABERTO, Pedido.STATUS_PAGO, Pedido.STATUS_CANCELADO
    };

    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("paginaAtiva", "pedidos");

        String acao = request.getParameter("acao");
        if (acao == null) {
            acao = "listar";
        }

        try {
            switch (acao) {
                case "novo":
                    tratarNovo(request, response);
                    break;

                case "editar":
                    tratarEditar(request, response);
                    break;

                case "excluir":
                    tratarExcluir(request, response);
                    break;

                case "listar":
                default:
                    listarTodos(request, response);
                    break;
            }
        } catch (SQLException e) {
            tratarErroBanco(request, response, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("paginaAtiva", "pedidos");

        String acao = request.getParameter("acao");

        try {
            if ("salvar".equals(acao)) {
                tratarSalvar(request, response);
            } else {
                response.sendRedirect("pedidos?acao=listar");
            }
        } catch (SQLException e) {
            tratarErroBanco(request, response, e);
        }
    }

    // ------------------------------------------------------------------
    // Operações (Create / Read / Update / Delete)
    // ------------------------------------------------------------------

    private void listarTodos(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        int clienteId = parseId(request.getParameter("clienteId"));

        List<Pedido> pedidos = (clienteId > 0)
                ? pedidoDAO.listarPorCliente(clienteId)
                : pedidoDAO.listar();

        request.setAttribute("pedidos", pedidos);

        if (clienteId > 0) {
            request.setAttribute("clienteFiltro", clienteDAO.buscarPorId(clienteId));
        }

        request.getRequestDispatcher("/WEB-INF/views/pedidos/listar.jsp").forward(request, response);
    }

    private void tratarNovo(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        Pedido pedido = new Pedido();
        pedido.setDataPedido(LocalDate.now());

        // Se veio de "Pedidos" na tela de um cliente específico, já
        // deixa esse cliente pré-selecionado no formulário.
        int clienteId = parseId(request.getParameter("clienteId"));
        if (clienteId > 0) {
            pedido.setClienteId(clienteId);
        }

        exibirFormulario(request, response, pedido);
    }

    private void tratarEditar(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        int id = parseId(request.getParameter("id"));
        Pedido pedido = (id > 0) ? pedidoDAO.buscarPorId(id) : null;

        if (pedido == null) {
            request.setAttribute("mensagemErro", "Pedido não encontrado (id=" + id + ").");
            listarTodos(request, response);
            return;
        }

        exibirFormulario(request, response, pedido);
    }

    private void exibirFormulario(HttpServletRequest request, HttpServletResponse response,
            Pedido pedido) throws SQLException, ServletException, IOException {

        request.setAttribute("pedido", pedido);
        request.setAttribute("clientes", clienteDAO.listar());
        request.setAttribute("statusValidos", STATUS_VALIDOS);
        request.getRequestDispatcher("/WEB-INF/views/pedidos/form.jsp").forward(request, response);
    }

    private void tratarSalvar(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        int id = parseId(request.getParameter("id"));
        int clienteId = parseId(request.getParameter("clienteId"));
        String status = valor(request.getParameter("status"));
        String dataPedidoTexto = valor(request.getParameter("dataPedido"));

        List<ItemPedido> itens = montarItens(
                request.getParameterValues("produto[]"),
                request.getParameterValues("quantidade[]"),
                request.getParameterValues("precoUnitario[]"));

        LocalDate dataPedido = parseData(dataPedidoTexto);

        String erro = validar(clienteId, dataPedido, status, itens);
        if (erro != null) {
            Pedido pedido = new Pedido();
            pedido.setId(id);
            pedido.setClienteId(clienteId);
            pedido.setDataPedido(dataPedido);
            pedido.setStatus(status.isBlank() ? Pedido.STATUS_ABERTO : status);
            pedido.setItens(itens);

            request.setAttribute("mensagemErro", erro);
            exibirFormulario(request, response, pedido);
            return;
        }

        Pedido pedido = new Pedido();
        pedido.setId(id);
        pedido.setClienteId(clienteId);
        pedido.setDataPedido(dataPedido);
        pedido.setStatus(status);
        pedido.setItens(itens);

        if (id > 0) {
            pedidoDAO.alterar(pedido);
        } else {
            pedidoDAO.cadastrar(pedido);
        }

        response.sendRedirect("pedidos?acao=listar");
    }

    private void tratarExcluir(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {

        int id = parseId(request.getParameter("id"));
        if (id > 0) {
            pedidoDAO.excluir(id);
        }
        response.sendRedirect("pedidos?acao=listar");
    }

    // ------------------------------------------------------------------
    // Auxiliares
    // ------------------------------------------------------------------

    /**
     * Monta a lista de itens a partir dos arrays paralelos enviados pelo
     * formulário (produto[], quantidade[], precoUnitario[] — uma posição
     * por linha da tabela de itens montada em JavaScript no form.jsp).
     * Linhas com produto em branco são ignoradas.
     */
    private List<ItemPedido> montarItens(String[] produtos, String[] quantidades, String[] precos) {
        List<ItemPedido> itens = new ArrayList<>();

        if (produtos == null) {
            return itens;
        }

        for (int i = 0; i < produtos.length; i++) {
            String produto = (produtos[i] == null) ? "" : produtos[i].trim();
            if (produto.isEmpty()) {
                continue;
            }

            int quantidade = parseInt(valorArray(quantidades, i));
            double precoUnitario = parseDouble(valorArray(precos, i));

            itens.add(new ItemPedido(produto, quantidade, precoUnitario));
        }

        return itens;
    }

    private String validar(int clienteId, LocalDate dataPedido, String status, List<ItemPedido> itens) {
        if (clienteId <= 0) {
            return "Selecione um cliente para o pedido.";
        }
        if (dataPedido == null) {
            return "Informe uma data de pedido válida.";
        }
        if (status == null || status.isBlank()) {
            return "Selecione um status para o pedido.";
        }
        if (itens.isEmpty()) {
            return "O pedido precisa ter ao menos um item.";
        }
        for (ItemPedido item : itens) {
            if (item.getQuantidade() <= 0) {
                return "A quantidade do item \"" + item.getProduto() + "\" deve ser maior que zero.";
            }
            if (item.getPrecoUnitario() < 0) {
                return "O preço unitário do item \"" + item.getProduto() + "\" não pode ser negativo.";
            }
        }
        return null;
    }

    private LocalDate parseData(String texto) {
        try {
            return (texto == null || texto.isBlank()) ? null : LocalDate.parse(texto.trim());
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private int parseId(String valor) {
        return parseInt(valor);
    }

    private int parseInt(String valor) {
        try {
            return (valor == null || valor.isBlank()) ? 0 : Integer.parseInt(valor.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private double parseDouble(String valor) {
        try {
            return (valor == null || valor.isBlank()) ? 0 : Double.parseDouble(valor.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private String valorArray(String[] array, int indice) {
        return (array != null && indice < array.length) ? array[indice] : null;
    }

    private String valor(String texto) {
        return (texto == null) ? "" : texto.trim();
    }

    private void tratarErroBanco(HttpServletRequest request, HttpServletResponse response,
            SQLException e) throws ServletException, IOException {

        e.printStackTrace();

        request.setAttribute("mensagemErro",
                "Não foi possível comunicar com o banco de dados. "
                + "Verifique se o MySQL está em execução e se as tabelas 'pedido' e "
                + "'item_pedido' existem (sql/schema.sql).");
        request.setAttribute("detalheErro", e.getMessage());
        request.getRequestDispatcher("/WEB-INF/views/erro.jsp").forward(request, response);
    }
}
