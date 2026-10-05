package br.com.sistemacad.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

import br.com.sistemacad.dao.ClienteDAO;
import br.com.sistemacad.modelo.Cliente;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet responsável por toda a camada web do CRUD de clientes.
 *
 * Fluxo da aplicação 
 *
 *   Navegador (JSP)
 *        |
 *        v
 *   ClienteServlet   <-- você está aqui
 *        |
 *        v
 *   ClienteDAO
 *        |
 *        v
 *   PreparedStatement / Connection / Driver JDBC
 *        |
 *        v
 *   MySQL
 *
 * Todas as requisições passam por um único parâmetro "acao", que
 * define o que o servlet deve fazer. Essa é uma forma simples e
 * didática de organizar um Servlet que centraliza várias operações.
 */
@WebServlet("/clientes")
public class ClienteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Pattern EMAIL_REGEX =
            Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");

    private final ClienteDAO clienteDAO = new ClienteDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acao = request.getParameter("acao");
        if (acao == null) {
            acao = "listar";
        }

        try {
            switch (acao) {
                case "novo":
                    exibirFormulario(request, response, new Cliente());
                    break;

                case "editar":
                    tratarEditar(request, response);
                    break;

                case "excluir":
                    tratarExcluir(request, response);
                    break;

                case "buscar":
                    tratarBuscaPorCidade(request, response);
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

        String acao = request.getParameter("acao");

        try {
            if ("salvar".equals(acao)) {
                tratarSalvar(request, response);
            } else {
                response.sendRedirect("clientes?acao=listar");
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

        List<Cliente> clientes = clienteDAO.listar();
        request.setAttribute("clientes", clientes);
        request.getRequestDispatcher("/WEB-INF/views/listar.jsp").forward(request, response);
    }

    private void tratarBuscaPorCidade(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        String cidade = request.getParameter("cidade");
        List<Cliente> clientes;

        if (cidade == null || cidade.isBlank()) {
            clientes = clienteDAO.listar();
        } else {
            clientes = clienteDAO.buscarPorCidade(cidade.trim());
        }

        request.setAttribute("clientes", clientes);
        request.setAttribute("filtroCidade", cidade);
        request.getRequestDispatcher("/WEB-INF/views/listar.jsp").forward(request, response);
    }

    private void tratarEditar(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        int id = parseId(request.getParameter("id"));
        Cliente cliente = (id > 0) ? clienteDAO.buscarPorId(id) : null;

        if (cliente == null) {
            request.setAttribute("mensagemErro", "Cliente não encontrado (id=" + id + ").");
            listarTodos(request, response);
            return;
        }

        exibirFormulario(request, response, cliente);
    }

    private void exibirFormulario(HttpServletRequest request, HttpServletResponse response,
            Cliente cliente) throws ServletException, IOException {

        request.setAttribute("cliente", cliente);
        request.getRequestDispatcher("/WEB-INF/views/form.jsp").forward(request, response);
    }

    private void tratarSalvar(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        int id = parseId(request.getParameter("id"));
        String nome = valor(request.getParameter("nome"));
        String email = valor(request.getParameter("email"));
        String cidade = valor(request.getParameter("cidade"));

        String erro = validar(nome, email);
        if (erro != null) {
            Cliente cliente = new Cliente(id, nome, email, cidade);
            request.setAttribute("cliente", cliente);
            request.setAttribute("mensagemErro", erro);
            request.getRequestDispatcher("/WEB-INF/views/form.jsp").forward(request, response);
            return;
        }

        Cliente cliente = new Cliente(id, nome, email, cidade);

        if (id > 0) {
            clienteDAO.alterar(cliente);
        } else {
            clienteDAO.cadastrar(cliente);
        }

        response.sendRedirect("clientes?acao=listar");
    }

    private void tratarExcluir(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {

        int id = parseId(request.getParameter("id"));
        if (id > 0) {
            clienteDAO.excluir(id);
        }
        response.sendRedirect("clientes?acao=listar");
    }

    // ------------------------------------------------------------------
    // Auxiliares
    // ------------------------------------------------------------------

    private String validar(String nome, String email) {
        if (nome == null || nome.isBlank()) {
            return "O campo Nome é obrigatório.";
        }
        if (nome.length() > 100) {
            return "O campo Nome deve ter no máximo 100 caracteres.";
        }
        if (email == null || email.isBlank()) {
            return "O campo E-mail é obrigatório.";
        }
        if (!EMAIL_REGEX.matcher(email).matches()) {
            return "Informe um e-mail válido.";
        }
        return null;
    }

    private int parseId(String valor) {
        try {
            return (valor == null || valor.isBlank()) ? 0 : Integer.parseInt(valor.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private String valor(String texto) {
        return (texto == null) ? "" : texto.trim();
    }

    private void tratarErroBanco(HttpServletRequest request, HttpServletResponse response,
            SQLException e) throws ServletException, IOException {

        // Log no console do servidor (visível na aba Console do Eclipse/Tomcat)
        e.printStackTrace();

        request.setAttribute("mensagemErro",
                "Não foi possível comunicar com o banco de dados. "
                + "Verifique se o MySQL está em execução e se o banco 'sistemacad' existe.");
        request.setAttribute("detalheErro", e.getMessage());
        request.getRequestDispatcher("/WEB-INF/views/erro.jsp").forward(request, response);
    }
}
