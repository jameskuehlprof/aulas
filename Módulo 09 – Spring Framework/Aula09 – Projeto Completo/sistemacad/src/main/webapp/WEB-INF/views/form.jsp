<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>SistemaCad - ${empty cliente.id or cliente.id == 0 ? 'Novo cliente' : 'Editar cliente'}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<%@ include file="/WEB-INF/views/_nav.jspf" %>

<main class="container pb-5" style="max-width: 640px;">

    <h1 class="h3 mb-3">${empty cliente.id or cliente.id == 0 ? 'Novo cliente' : 'Editar cliente'}</h1>

    <c:if test="${not empty mensagemErro}">
        <div class="alert alert-danger">${mensagemErro}</div>
    </c:if>

    <div class="card">
        <div class="card-body">
            <form method="post" action="${pageContext.request.contextPath}/clientes">
                <input type="hidden" name="acao" value="salvar">
                <input type="hidden" name="id" value="${cliente.id}">

                <div class="mb-3">
                    <label class="form-label" for="nome">Nome</label>
                    <input type="text" class="form-control" id="nome" name="nome"
                           maxlength="100" required value="${cliente.nome}">
                </div>

                <div class="mb-3">
                    <label class="form-label" for="email">E-mail</label>
                    <input type="email" class="form-control" id="email" name="email"
                           maxlength="150" required value="${cliente.email}">
                </div>

                <div class="mb-3">
                    <label class="form-label" for="cidade">Cidade</label>
                    <input type="text" class="form-control" id="cidade" name="cidade"
                           maxlength="100" value="${cliente.cidade}">
                </div>

                <div class="d-flex gap-2">
                    <button class="btn btn-primary" type="submit">Salvar</button>
                    <a class="btn btn-outline-secondary"
                       href="${pageContext.request.contextPath}/clientes?acao=listar">Cancelar</a>
                </div>
            </form>
        </div>
    </div>
</main>

<footer class="text-center text-muted small py-4">
    SistemaCad &middot; Aula 07 &mdash; Pedido / ItemPedido vinculados ao CRUD de Clientes
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
