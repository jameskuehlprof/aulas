<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // Página de entrada da aplicação: apenas redireciona para o Servlet responsável por listar os clientes.
    response.sendRedirect(request.getContextPath() + "/clientes");
%>
