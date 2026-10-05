<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>LojavirtualInterface - Erro</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<%@ include file="/WEB-INF/views/_nav.jspf" %>

<main class="container pb-5" style="max-width: 640px;">
    <div class="card">
        <div class="card-body">
            <h1 class="h4">Ops! A API respondeu com um problema</h1>
            <div class="alert alert-danger">${mensagemErro}</div>

            <c:if test="${not empty statusErro and statusErro == 0}">
                <p class="text-muted small">
                    Status 0 geralmente significa que a API lojavirtual não está em execução,
                    ou está em outra porta. Confira em
                    <code>br.com.exemplo.lojavirtualinterface.config.ApiConfig</code>
                    se BASE_URL bate com o server.port do lojavirtual (padrão: 8080).
                </p>
            </c:if>

            <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/">Voltar ao início</a>
        </div>
    </div>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
