package br.com.exemplo.lojavirtualinterface.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

import br.com.exemplo.lojavirtualinterface.client.ApiClient;
import br.com.exemplo.lojavirtualinterface.client.ApiException;
import br.com.exemplo.lojavirtualinterface.modelo.ClienteDTO;
import br.com.exemplo.lojavirtualinterface.modelo.ItemPedidoRequestDTO;
import br.com.exemplo.lojavirtualinterface.modelo.PedidoDTO;
import br.com.exemplo.lojavirtualinterface.modelo.PedidoRequestDTO;
import br.com.exemplo.lojavirtualinterface.modelo.ProdutoDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet de Pedidos. Diferente dos outros três: a API não tem PUT em
 * /pedidos (decisão de design do próprio lojavirtual — um pedido é
 * auditável, não se edita silenciosamente, só se cancela/exclui), então
 * este Servlet não tem ação "editar", só "novo" (criar), "listar" e
 * "excluir". Os itens de cada pedido já vêm dentro do PedidoDTO retornado
 * por GET /pedidos, então a listagem consegue mostrá-los sem nenhuma
 * chamada extra à API (ver listar.jsp, seção recolhível por linha).
 */
@WebServlet("/pedidos")
public class PedidoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

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
                    exibirFormulario(request, response);
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

        request.setAttribute("paginaAtiva", "pedidos");

        try {
            if ("salvar".equals(request.getParameter("acao"))) {
                tratarSalvar(request, response);
            } else {
                response.sendRedirect("pedidos?acao=listar");
            }
        } catch (ApiException e) {
            tratarErroApi(request, response, e);
        }
    }

    // ------------------------------------------------------------------

    private void listarTodos(HttpServletRequest request, HttpServletResponse response)
            throws ApiException, ServletException, IOException {

        Long clienteId = parseId(request.getParameter("clienteId"));
        List<PedidoDTO> pedidos;

        if (clienteId != null) {
            pedidos = ApiClient.get("/pedidos/cliente/" + clienteId, new TypeReference<List<PedidoDTO>>() {
            });
            request.setAttribute("clienteFiltro", ApiClient.get("/clientes/" + clienteId, ClienteDTO.class));
        } else {
            pedidos = ApiClient.get("/pedidos", new TypeReference<List<PedidoDTO>>() {
            });
        }

        request.setAttribute("pedidos", pedidos);
        request.getRequestDispatcher("/WEB-INF/views/pedidos/listar.jsp").forward(request, response);
    }

    private void exibirFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ApiException, ServletException, IOException {

        List<ClienteDTO> clientes = ApiClient.get("/clientes", new TypeReference<List<ClienteDTO>>() {
        });
        List<ProdutoDTO> produtos = ApiClient.get("/produtos", new TypeReference<List<ProdutoDTO>>() {
        });

        request.setAttribute("clientes", clientes);
        request.setAttribute("produtos", produtos);
        request.setAttribute("clienteIdPreSelecionado", request.getParameter("clienteId"));
        request.getRequestDispatcher("/WEB-INF/views/pedidos/form.jsp").forward(request, response);
    }

    private void tratarSalvar(HttpServletRequest request, HttpServletResponse response)
            throws ApiException, ServletException, IOException {

        Long clienteId = parseId(request.getParameter("clienteId"));
        String[] produtoIds = request.getParameterValues("produtoId[]");
        String[] quantidades = request.getParameterValues("quantidade[]");

        List<ItemPedidoRequestDTO> itens = new ArrayList<>();
        if (produtoIds != null && quantidades != null) {
            for (int i = 0; i < produtoIds.length && i < quantidades.length; i++) {
                Long produtoId = parseId(produtoIds[i]);
                Integer quantidade = parseInt(quantidades[i]);
                if (produtoId != null && quantidade != null && quantidade > 0) {
                    itens.add(new ItemPedidoRequestDTO(produtoId, quantidade));
                }
            }
        }

        if (clienteId == null || itens.isEmpty()) {
            // Mesma regra que a API já valida (@NotNull no clienteId,
            // @NotEmpty nos itens) — checamos aqui também só para dar uma
            // mensagem amigável em vez de deixar a API devolver 400 puro
            // sem o formulário reaparecer preenchido.
            request.setAttribute("mensagemErro",
                    "Selecione um cliente e adicione ao menos um item válido antes de salvar.");
            exibirFormulario(request, response);
            return;
        }

        PedidoRequestDTO dto = new PedidoRequestDTO(clienteId, itens);
        ApiClient.post("/pedidos", dto, PedidoDTO.class);

        response.sendRedirect("pedidos?acao=listar");
    }

    private void tratarExcluir(HttpServletRequest request, HttpServletResponse response)
            throws ApiException, IOException {

        Long id = parseId(request.getParameter("id"));
        if (id != null) {
            ApiClient.delete("/pedidos/" + id);
        }
        response.sendRedirect("pedidos?acao=listar");
    }

    // ------------------------------------------------------------------

    private Long parseId(String valor) {
        try {
            return (valor == null || valor.isBlank()) ? null : Long.valueOf(valor.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Integer parseInt(String valor) {
        try {
            return (valor == null || valor.isBlank()) ? null : Integer.valueOf(valor.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void tratarErroApi(HttpServletRequest request, HttpServletResponse response, ApiException e)
            throws ServletException, IOException {

        e.printStackTrace();
        request.setAttribute("mensagemErro", e.getMessage());
        request.setAttribute("statusErro", e.getStatus());
        request.getRequestDispatcher("/WEB-INF/views/erro.jsp").forward(request, response);
    }
}
