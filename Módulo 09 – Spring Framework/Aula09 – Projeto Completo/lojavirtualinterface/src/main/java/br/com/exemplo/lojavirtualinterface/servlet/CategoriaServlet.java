package br.com.exemplo.lojavirtualinterface.servlet;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

import br.com.exemplo.lojavirtualinterface.client.ApiClient;
import br.com.exemplo.lojavirtualinterface.client.ApiException;
import br.com.exemplo.lojavirtualinterface.modelo.CategoriaDTO;
import br.com.exemplo.lojavirtualinterface.modelo.CategoriaRequestDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet do CRUD de Categorias. Mesmo desenho do ClienteServlet do
 * sistemacad (Módulo 08) — um único parâmetro "acao" concentra as
 * operações — só que aqui, em vez de um DAO, quem resolve cada operação
 * é o ApiClient, chamando GET/POST/PUT/DELETE /categorias na API.
 *
 *   Navegador (JSP)
 *        |
 *        v
 *   CategoriaServlet   <-- você está aqui
 *        |
 *        v
 *   ApiClient (HTTP/JSON)
 *        |
 *        v
 *   API lojavirtual (Spring Boot) -> CategoriaController -> ... -> MySQL
 */
@WebServlet("/categorias")
public class CategoriaServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("paginaAtiva", "categorias");

        String acao = request.getParameter("acao");
        if (acao == null) {
            acao = "listar";
        }

        try {
            switch (acao) {
                case "novo":
                    exibirFormulario(request, response, new CategoriaDTO());
                    break;

                case "editar":
                    tratarEditar(request, response);
                    break;

                case "excluir":
                    tratarExcluir(request, response);
                    break;

                case "listar":
                default:
                    listarTodas(request, response);
                    break;
            }
        } catch (ApiException e) {
            tratarErroApi(request, response, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("paginaAtiva", "categorias");

        try {
            if ("salvar".equals(request.getParameter("acao"))) {
                tratarSalvar(request, response);
            } else {
                response.sendRedirect("categorias?acao=listar");
            }
        } catch (ApiException e) {
            tratarErroApi(request, response, e);
        }
    }

    // ------------------------------------------------------------------

    private void listarTodas(HttpServletRequest request, HttpServletResponse response)
            throws ApiException, ServletException, IOException {

        List<CategoriaDTO> categorias = ApiClient.get("/categorias", new TypeReference<List<CategoriaDTO>>() {
        });
        request.setAttribute("categorias", categorias);
        request.getRequestDispatcher("/WEB-INF/views/categorias/listar.jsp").forward(request, response);
    }

    private void tratarEditar(HttpServletRequest request, HttpServletResponse response)
            throws ApiException, ServletException, IOException {

        Long id = parseId(request.getParameter("id"));
        if (id == null) {
            response.sendRedirect("categorias?acao=listar");
            return;
        }
        CategoriaDTO categoria = ApiClient.get("/categorias/" + id, CategoriaDTO.class);
        exibirFormulario(request, response, categoria);
    }

    private void exibirFormulario(HttpServletRequest request, HttpServletResponse response, CategoriaDTO categoria)
            throws ServletException, IOException {

        request.setAttribute("categoria", categoria);
        request.getRequestDispatcher("/WEB-INF/views/categorias/form.jsp").forward(request, response);
    }

    private void tratarSalvar(HttpServletRequest request, HttpServletResponse response)
            throws ApiException, IOException {

        Long id = parseId(request.getParameter("id"));
        String nome = valor(request.getParameter("nome"));

        CategoriaRequestDTO dto = new CategoriaRequestDTO(nome);

        if (id != null) {
            ApiClient.put("/categorias/" + id, dto, CategoriaDTO.class);
        } else {
            ApiClient.post("/categorias", dto, CategoriaDTO.class);
        }

        response.sendRedirect("categorias?acao=listar");
    }

    private void tratarExcluir(HttpServletRequest request, HttpServletResponse response)
            throws ApiException, IOException {

        Long id = parseId(request.getParameter("id"));
        if (id != null) {
            ApiClient.delete("/categorias/" + id);
        }
        response.sendRedirect("categorias?acao=listar");
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
