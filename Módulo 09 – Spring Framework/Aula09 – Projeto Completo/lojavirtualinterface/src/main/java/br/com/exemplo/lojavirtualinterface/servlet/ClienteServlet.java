package br.com.exemplo.lojavirtualinterface.servlet;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

import br.com.exemplo.lojavirtualinterface.client.ApiClient;
import br.com.exemplo.lojavirtualinterface.client.ApiException;
import br.com.exemplo.lojavirtualinterface.modelo.ClienteDTO;
import br.com.exemplo.lojavirtualinterface.modelo.ClienteRequestDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet do CRUD de Clientes — mesmo desenho do CategoriaServlet.
 * ClienteDTO (o que a API devolve) não tem CPF, só id/nome/email; CPF só
 * existe do lado do RequestDTO (entrada), então o formulário de edição
 * não consegue pré-preencher o campo cpf — igual à própria API, que
 * também não devolve esse dado depois de cadastrado.
 */
@WebServlet("/clientes")
public class ClienteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("paginaAtiva", "clientes");

        String acao = request.getParameter("acao");
        if (acao == null) {
            acao = "listar";
        }

        try {
            switch (acao) {
                case "novo":
                    exibirFormulario(request, response, new ClienteDTO());
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
        } catch (ApiException e) {
            tratarErroApi(request, response, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("paginaAtiva", "clientes");

        try {
            if ("salvar".equals(request.getParameter("acao"))) {
                tratarSalvar(request, response);
            } else {
                response.sendRedirect("clientes?acao=listar");
            }
        } catch (ApiException e) {
            tratarErroApi(request, response, e);
        }
    }

    // ------------------------------------------------------------------

    private void listarTodos(HttpServletRequest request, HttpServletResponse response)
            throws ApiException, ServletException, IOException {

        List<ClienteDTO> clientes = ApiClient.get("/clientes", new TypeReference<List<ClienteDTO>>() {
        });
        request.setAttribute("clientes", clientes);
        request.getRequestDispatcher("/WEB-INF/views/clientes/listar.jsp").forward(request, response);
    }

    private void tratarEditar(HttpServletRequest request, HttpServletResponse response)
            throws ApiException, ServletException, IOException {

        Long id = parseId(request.getParameter("id"));
        if (id == null) {
            response.sendRedirect("clientes?acao=listar");
            return;
        }
        ClienteDTO cliente = ApiClient.get("/clientes/" + id, ClienteDTO.class);
        exibirFormulario(request, response, cliente);
    }

    private void exibirFormulario(HttpServletRequest request, HttpServletResponse response, ClienteDTO cliente)
            throws ServletException, IOException {

        request.setAttribute("cliente", cliente);
        request.getRequestDispatcher("/WEB-INF/views/clientes/form.jsp").forward(request, response);
    }

    private void tratarSalvar(HttpServletRequest request, HttpServletResponse response)
            throws ApiException, IOException {

        Long id = parseId(request.getParameter("id"));
        String nome = valor(request.getParameter("nome"));
        String email = valor(request.getParameter("email"));
        String cpf = valor(request.getParameter("cpf"));

        ClienteRequestDTO dto = new ClienteRequestDTO(nome, email, cpf);

        if (id != null) {
            ApiClient.put("/clientes/" + id, dto, ClienteDTO.class);
        } else {
            ApiClient.post("/clientes", dto, ClienteDTO.class);
        }

        response.sendRedirect("clientes?acao=listar");
    }

    private void tratarExcluir(HttpServletRequest request, HttpServletResponse response)
            throws ApiException, IOException {

        Long id = parseId(request.getParameter("id"));
        if (id != null) {
            ApiClient.delete("/clientes/" + id);
        }
        response.sendRedirect("clientes?acao=listar");
    }

    // ------------------------------------------------------------------

    private Long parseId(String valor) {
        try {
            return (valor == null || valor.isBlank()) ? null : Long.valueOf(valor.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String valor(String texto) {
        return (texto == null) ? "" : texto.trim();
    }

    private void tratarErroApi(HttpServletRequest request, HttpServletResponse response, ApiException e)
            throws ServletException, IOException {

        e.printStackTrace();
        request.setAttribute("mensagemErro", e.getMessage());
        request.setAttribute("statusErro", e.getStatus());
        request.getRequestDispatcher("/WEB-INF/views/erro.jsp").forward(request, response);
    }
}
