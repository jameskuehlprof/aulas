<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>SistemaCad - Erro</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<%@ include file="/WEB-INF/views/_nav.jspf" %>

<main class="container pb-5" style="max-width: 640px;">
    <div class="card">
        <div class="card-body">
            <h1 class="h4">Ops! Ocorreu um problema</h1>
            <div class="alert alert-danger">${mensagemErro}</div>

            <% if (request.getAttribute("detalheErro") != null) { %>
                <p class="text-muted small">Detalhe técnico: ${detalheErro}</p>
            <% } %>

            <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/clientes">Voltar</a>
        </div>
    </div>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
