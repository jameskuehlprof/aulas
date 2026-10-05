<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>LojavirtualInterface - Produtos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<%@ include file="/WEB-INF/views/_nav.jspf" %>

<main class="container pb-5">

    <div class="d-flex justify-content-between align-items-center flex-wrap gap-2 mb-3">
        <div>
            <h1 class="h3 mb-0">Produtos</h1>
            <p class="text-muted small mb-0">
                GET/POST/PUT/DELETE /produtos na API lojavirtual
                <c:if test="${not empty categoriaFiltro}">
                    &mdash; categoria: <strong>${categoriaFiltro.nome}</strong>
                </c:if>
            </p>
        </div>
        <div class="d-flex gap-2">
            <c:if test="${not empty categoriaFiltro}">
                <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/produtos">Ver todos</a>
            </c:if>
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/produtos?acao=novo">+ Novo produto</a>
        </div>
    </div>

    <c:if test="${not empty mensagemErro}">
        <div class="alert alert-danger">${mensagemErro}</div>
    </c:if>

    <div class="card">
        <div class="card-body">

            <form class="row g-2 mb-3" method="get" action="${pageContext.request.contextPath}/produtos">
                <div class="col-auto flex-grow-1">
                    <input type="text" class="form-control" name="nome"
                           placeholder="Buscar por nome..." value="${filtroNome}">
                </div>
                <div class="col-auto">
                    <button class="btn btn-outline-secondary" type="submit">Buscar</button>
                </div>
                <c:if test="${not empty filtroNome}">
                    <div class="col-auto">
                        <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/produtos">Limpar</a>
                    </div>
                </c:if>
            </form>

            <c:choose>
                <c:when test="${empty produtos}">
                    <p class="text-center text-muted py-4 mb-0">Nenhum produto encontrado.</p>
                </c:when>
                <c:otherwise>
                    <div class="table-responsive">
                        <table class="table table-hover align-middle mb-0">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Nome</th>
                                    <th>Categoria</th>
                                    <th class="text-end">Preço</th>
                                    <th class="text-end">Ações</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="produto" items="${produtos}">
                                    <tr>
                                        <td><span class="badge text-bg-light border">#${produto.id}</span></td>
                                        <td>${produto.nome}</td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/produtos?categoriaId=${produto.categoriaId}">
                                                ${produto.categoriaNome}
                                            </a>
                                        </td>
                                        <td class="text-end">R$ <fmt:formatNumber value="${produto.preco}" pattern="0.00"/></td>
                                        <td class="text-end">
                                            <div class="btn-group btn-group-sm" role="group">
                                                <a class="btn btn-outline-secondary"
                                                   href="${pageContext.request.contextPath}/produtos?acao=editar&id=${produto.id}">Editar</a>
                                                <a class="btn btn-outline-danger"
                                                   href="${pageContext.request.contextPath}/produtos?acao=excluir&id=${produto.id}"
                                                   onclick="return confirm('Excluir o produto ' + '${produto.nome}' + '?');">Excluir</a>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</main>

<footer class="text-center text-muted small py-4">
    LojavirtualInterface &middot; Aula 09 &mdash; interface gráfica para a API lojavirtual
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
