package br.com.exemplo.lojavirtualinterface.servlet;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

import br.com.exemplo.lojavirtualinterface.client.ApiClient;
import br.com.exemplo.lojavirtualinterface.client.ApiException;
import br.com.exemplo.lojavirtualinterface.modelo.RelatorioDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet do Relatório de Vendas por Categoria (exercício da Aula 06).
 * É só leitura: um único GET /relatorios/total-por-categoria, sem "acao"
 * nenhuma, porque não existe outra operação sobre um relatório — ele não
 * é uma entidade que se cria/edita/exclui, é uma consulta agregada que a
 * própria API já devolve pronta e ordenada (maior total primeiro).
 */
@WebServlet("/relatorios")
public class RelatorioServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("paginaAtiva", "relatorios");

        try {
            List<RelatorioDTO> relatorio = ApiClient.get(
                    "/relatorios/total-por-categoria", new TypeReference<List<RelatorioDTO>>() {
            });
            request.setAttribute("relatorio", relatorio);
            request.getRequestDispatcher("/WEB-INF/views/relatorios/listar.jsp").forward(request, response);
        } catch (ApiException e) {
            e.printStackTrace();
            request.setAttribute("mensagemErro", e.getMessage());
            request.setAttribute("statusErro", e.getStatus());
            request.getRequestDispatcher("/WEB-INF/views/erro.jsp").forward(request, response);
        }
    }
}
