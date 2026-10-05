<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>LojavirtualInterface - Clientes</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<%@ include file="/WEB-INF/views/_nav.jspf" %>

<main class="container pb-5">

    <div class="d-flex justify-content-between align-items-center flex-wrap gap-2 mb-3">
        <div>
            <h1 class="h3 mb-0">Clientes</h1>
            <p class="text-muted small mb-0">GET/POST/PUT/DELETE /clientes na API lojavirtual</p>
        </div>
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/clientes?acao=novo">+ Novo cliente</a>
    </div>

    <c:if test="${not empty mensagemErro}">
        <div class="alert alert-danger">${mensagemErro}</div>
    </c:if>

    <div class="card">
        <div class="card-body">
            <c:choose>
                <c:when test="${empty clientes}">
                    <p class="text-center text-muted py-4 mb-0">Nenhum cliente cadastrado.</p>
                </c:when>
                <c:otherwise>
                    <div class="table-responsive">
                        <table class="table table-hover align-middle mb-0">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Nome</th>
                                    <th>E-mail</th>
                                    <th class="text-end">Ações</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="cliente" items="${clientes}">
                                    <tr>
                                        <td><span class="badge text-bg-light border">#${cliente.id}</span></td>
                                        <td>${cliente.nome}</td>
                                        <td>${cliente.email}</td>
                                        <td class="text-end">
                                            <div class="btn-group btn-group-sm" role="group">
                                                <a class="btn btn-outline-secondary"
                                                   href="${pageContext.request.contextPath}/pedidos?clienteId=${cliente.id}"
                                                   title="Ver pedidos deste cliente">Pedidos</a>
                                                <a class="btn btn-outline-secondary"
                                                   href="${pageContext.request.contextPath}/clientes?acao=editar&id=${cliente.id}">Editar</a>
                                                <a class="btn btn-outline-danger"
                                                   href="${pageContext.request.contextPath}/clientes?acao=excluir&id=${cliente.id}"
                                                   onclick="return confirm('Excluir o cliente ' + '${cliente.nome}' + '?');">Excluir</a>
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
