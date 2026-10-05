<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String nome = request.getParameter("nome");
String idade = request.getParameter("idade");
int idadeNumero = Integer.parseInt(idade);
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Resultado</title>
</head>
<body>
<h1>Cadastro realizado!</h1>
<p>Nome: <%= nome %></p>
<p>Idade: <%= idadeNumero %></p>
<% if (idadeNumero >= 18) { %>
    <p>Você é maior de idade.</p>
<% } else { %>
    <p>Você é menor de idade.</p>
<% } %>
</body>
</html>