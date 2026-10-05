package br.com.exemplo.lojavirtualinterface.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;

import br.com.exemplo.lojavirtualinterface.client.ApiClient;
import br.com.exemplo.lojavirtualinterface.client.ApiException;
import br.com.exemplo.lojavirtualinterface.modelo.CategoriaDTO;
import br.com.exemplo.lojavirtualinterface.modelo.ProdutoDTO;
import br.com.exemplo.lojavirtualinterface.modelo.ProdutoRequestDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet do CRUD de Produtos. Mesmo desenho do CategoriaServlet — a
 * única diferença notável é que o formulário precisa da lista de
 * categorias (GET /categorias) para montar o &lt;select&gt;, então quem
 * exibe o formulário sempre busca as duas coisas.
 */
@WebServlet("/produtos")
public class ProdutoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("paginaAtiva", "produtos");

        String acao = request.getParameter("acao");
        if (acao == null) {
            acao = "listar";
        }

        try {
            switch (acao) {
                case "novo":
                    exibirFormulario(request, response, new ProdutoDTO());
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

        request.setAttribute("paginaAtiva", "produtos");

        try {
            if ("salvar".equals(request.getParameter("acao"))) {
                tratarSalvar(request, response);
            } else {
                response.sendRedirect("produtos?acao=listar");
            }
        } catch (ApiException e) {
            tratarErroApi(request, response, e);
        }
    }

    // ------------------------------------------------------------------

    private void listarTodos(HttpServletRequest request, HttpServletResponse response)
            throws ApiException, ServletException, IOException {

        // Suporta tanto o filtro por nome que a própria API já tem
        // (GET /produtos?nome=...) quanto o link "Produtos" que vem da
        // tela de Categorias (categoriaId), que a API não filtra: aqui
        // filtramos a lista já carregada, em memória.
        String nome = request.getParameter("nome");
        String caminho = "/produtos";
        if (nome != null && !nome.isBlank()) {
            caminho += "?nome=" + encodeParam(nome.trim());
        }

        List<ProdutoDTO> produtos = ApiClient.get(caminho, new TypeReference<List<ProdutoDTO>>() {
        });

        Long categoriaId = parseId(request.getParameter("categoriaId"));
        if (categoriaId != null) {
            produtos = produtos.stream()
                    .filter(p -> categoriaId.equals(p.getCategoriaId()))
                    .collect(Collectors.toList());
            request.setAttribute("categoriaFiltro", ApiClient.get("/categorias/" + categoriaId, CategoriaDTO.class));
        }

        request.setAttribute("produtos", produtos);
        request.setAttribute("filtroNome", nome);
        request.getRequestDispatcher("/WEB-INF/views/produtos/listar.jsp").forward(request, response);
    }

    private void tratarEditar(HttpServletRequest request, HttpServletResponse response)
            throws ApiException, ServletException, IOException {

        Long id = parseId(request.getParameter("id"));
        if (id == null) {
            response.sendRedirect("produtos?acao=listar");
            return;
        }
        ProdutoDTO produto = ApiClient.get("/produtos/" + id, ProdutoDTO.class);
        exibirFormulario(request, response, produto);
    }

    private void exibirFormulario(HttpServletRequest request, HttpServletResponse response, ProdutoDTO produto)
            throws ApiException, ServletException, IOException {

        List<CategoriaDTO> categorias = ApiClient.get("/categorias", new TypeReference<List<CategoriaDTO>>() {
        });
        request.setAttribute("categorias", categorias);
        request.setAttribute("produto", produto);
        request.getRequestDispatcher("/WEB-INF/views/produtos/form.jsp").forward(request, response);
    }

    private void tratarSalvar(HttpServletRequest request, HttpServletResponse response)
            throws ApiException, ServletException, IOException {

        Long id = parseId(request.getParameter("id"));
        String nome = valor(request.getParameter("nome"));
        BigDecimal preco = parseDecimal(request.getParameter("preco"));
        Long categoriaId = parseId(request.getParameter("categoriaId"));

        ProdutoRequestDTO dto = new ProdutoRequestDTO(nome, preco, categoriaId);

        if (id != null) {
            ApiClient.put("/produtos/" + id, dto, ProdutoDTO.class);
        } else {
            ApiClient.post("/produtos", dto, ProdutoDTO.class);
        }

        response.sendRedirect("produtos?acao=listar");
    }

    private void tratarExcluir(HttpServletRequest request, HttpServletResponse response)
            throws ApiException, IOException {

        Long id = parseId(request.getParameter("id"));
        if (id != null) {
            ApiClient.delete("/produtos/" + id);
        }
        response.sendRedirect("produtos?acao=listar");
    }

    // ------------------------------------------------------------------

    private Long parseId(String valor) {
        try {
            return (valor == null || valor.isBlank()) ? null : Long.valueOf(valor.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private BigDecimal parseDecimal(String valor) {
        try {
            return (valor == null || valor.isBlank()) ? null : new BigDecimal(valor.trim().replace(",", "."));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String valor(String texto) {
        return (texto == null) ? "" : texto.trim();
    }

    private String encodeParam(String texto) {
        try {
            return URLEncoder.encode(texto, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            return texto;
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
