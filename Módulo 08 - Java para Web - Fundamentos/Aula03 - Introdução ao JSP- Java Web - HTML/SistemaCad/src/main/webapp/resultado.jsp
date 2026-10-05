<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
    String nome = request.getParameter("nome");
    String idade = request.getParameter("idade");
    String email = request.getParameter("email");
    if (nome == null || nome.trim().isEmpty()) {
%>
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <title>Erro</title>
        </head>
        <body>
            <h1>Erro no cadastro</h1>
            <p>Nome é obrigatório!</p>
            <a href="cadastro.jsp">Voltar para o cadastro</a>
        </body>
        </html>

<%
    } else {
%>
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <title>Resultado</title>
        </head>
        <body>
            <h1>Cadastro realizado!</h1>
            <p>
                Olá, <strong><%= nome %></strong>!
            </p>
            <p>
                Idade: <%= idade %>
            </p>
            <p>
                E-mail: <%= email %>
            </p>
            <br>
            <a href="index.jsp">Voltar ao início</a>
        </body>
        </html>
<%
    }
%>