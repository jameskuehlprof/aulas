<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>SistemaCad - Erro</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <header class="topo">
        <h1>SistemaCad</h1>
        <p>Sistema de Cadastro de Clientes &mdash; Java Web + JDBC + MySQL</p>
    </header>

    <main class="container">
        <div class="card">
            <h2>Ops! Ocorreu um problema</h2>
            <div class="alerta alerta-erro">${mensagemErro}</div>

            <% if (request.getAttribute("detalheErro") != null) { %>
                <p style="font-size:12px;color:#a0aec0;">
                    Detalhe técnico: ${detalheErro}
                </p>
            <% } %>

            <a class="btn btn-secundario" href="${pageContext.request.contextPath}/clientes">
                Voltar
            </a>
        </div>
    </main>

</body>
</html>
