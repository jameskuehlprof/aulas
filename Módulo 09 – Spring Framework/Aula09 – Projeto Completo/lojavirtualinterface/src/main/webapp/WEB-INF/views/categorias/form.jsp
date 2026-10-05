<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>LojavirtualInterface - ${empty categoria.id ? 'Nova categoria' : 'Editar categoria'}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<%@ include file="/WEB-INF/views/_nav.jspf" %>

<main class="container pb-5" style="max-width: 640px;">

    <h1 class="h3 mb-3">${empty categoria.id ? 'Nova categoria' : 'Editar categoria'}</h1>

    <c:if test="${not empty mensagemErro}">
        <div class="alert alert-danger">${mensagemErro}</div>
    </c:if>

    <div class="card">
        <div class="card-body">
            <form method="post" action="${pageContext.request.contextPath}/categorias">
                <input type="hidden" name="acao" value="salvar">
                <input type="hidden" name="id" value="${categoria.id}">

                <div class="mb-3">
                    <label class="form-label" for="nome">Nome</label>
                    <input type="text" class="form-control" id="nome" name="nome"
                           minlength="3" maxlength="50" required value="${categoria.nome}">
                </div>

                <div class="d-flex gap-2">
                    <button class="btn btn-primary" type="submit">Salvar</button>
                    <a class="btn btn-outline-secondary"
                       href="${pageContext.request.contextPath}/categorias?acao=listar">Cancelar</a>
                </div>
            </form>
        </div>
    </div>
</main>

<footer class="text-center text-muted small py-4">
    LojavirtualInterface &middot; Aula 09 &mdash; interface gráfica para a API lojavirtual
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
