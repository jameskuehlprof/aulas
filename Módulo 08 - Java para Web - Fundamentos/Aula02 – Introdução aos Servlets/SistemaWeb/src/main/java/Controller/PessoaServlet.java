package Controller;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/pessoa")
public class PessoaServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        
        String nome = request.getParameter("nome");
       // String idade = request.getParameter("idade");
        
        int idade = Integer.parseInt(request.getParameter("idade"));
        
        int anoNascimento = 2026 - idade;
        
        PrintWriter out = response.getWriter();

        out.println("<h1>Dados da Pessoa</h1>");
        out.println("<p>Nome: " + nome + "</p>");
        out.println("<p>Idade: " + idade + "</p>");
        
        out.println("<p>Ano Nascimento: " + anoNascimento + "</p>");
        
        /* http://localhost:8080/SistemaWeb/pessoa
         * http://localhost:8080/SistemaWeb/pessoa?nome=James&idade=40
         *  */
    }
}