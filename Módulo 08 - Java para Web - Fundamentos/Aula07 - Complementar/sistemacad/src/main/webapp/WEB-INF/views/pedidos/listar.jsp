<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>SistemaCad - Pedidos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<%@ include file="/WEB-INF/views/_nav.jspf" %>

<main class="container pb-5">

    <div class="d-flex justify-content-between align-items-center flex-wrap gap-2 mb-3">
        <div>
            <h1 class="h3 mb-0">Pedidos</h1>
            <p class="text-muted small mb-0">
                Pedido (N) &rarr; Cliente (1) &middot; Pedido (1) &rarr; ItemPedido (N)
                <c:if test="${not empty clienteFiltro}">
                    &mdash; cliente: <strong>${clienteFiltro.nome}</strong>
                </c:if>
            </p>
        </div>
        <div class="d-flex gap-2">
            <c:if test="${not empty clienteFiltro}">
                <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/pedidos">Ver todos</a>
            </c:if>
            <%--
                Uma tag JSTL (<c:if>) não pode ficar dentro do valor de um
                atributo HTML (dentro das aspas de href="...") — por isso o
                link é montado por inteiro em cada ramo do c:choose abaixo,
                em vez de tentar concatenar um <c:if> junto com o texto do
                atributo.
            --%>
            <c:choose>
                <c:when test="${not empty clienteFiltro}">
                    <a class="btn btn-primary"
                       href="${pageContext.request.contextPath}/pedidos?acao=novo&clienteId=${clienteFiltro.id}">
                       + Novo pedido
                    </a>
                </c:when>
                <c:otherwise>
                    <a class="btn btn-primary" href="${pageContext.request.contextPath}/pedidos?acao=novo">
                       + Novo pedido
                    </a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <c:if test="${not empty mensagemErro}">
        <div class="alert alert-danger">${mensagemErro}</div>
    </c:if>

    <div class="card">
        <div class="card-body">
            <c:choose>
                <c:when test="${empty pedidos}">
                    <p class="text-center text-muted py-4 mb-0">Nenhum pedido encontrado.</p>
                </c:when>
                <c:otherwise>
                    <div class="table-responsive">
                        <table class="table table-hover align-middle mb-0">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Cliente</th>
                                    <th>Data</th>
                                    <th>Status</th>
                                    <th class="text-end">Total</th>
                                    <th class="text-end">Ações</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="pedido" items="${pedidos}">
                                    <tr>
                                        <td><span class="badge text-bg-light border">#${pedido.id}</span></td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/clientes?acao=editar&id=${pedido.clienteId}">
                                                ${pedido.clienteNome}
                                            </a>
                                        </td>
                                        <td>${pedido.dataPedido}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${pedido.status == 'PAGO'}">
                                                    <span class="badge text-bg-success">PAGO</span>
                                                </c:when>
                                                <c:when test="${pedido.status == 'CANCELADO'}">
                                                    <span class="badge text-bg-secondary">CANCELADO</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge text-bg-warning">ABERTO</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="text-end">
                                            R$ <fmt:formatNumber value="${pedido.valorTotal}" pattern="0.00"/>
                                        </td>
                                        <td class="text-end">
                                            <div class="btn-group btn-group-sm" role="group">
                                                <a class="btn btn-outline-secondary"
                                                   href="${pageContext.request.contextPath}/pedidos?acao=editar&id=${pedido.id}">Editar</a>
                                                <a class="btn btn-outline-danger"
                                                   href="${pageContext.request.contextPath}/pedidos?acao=excluir&id=${pedido.id}"
                                                   onclick="return confirm('Excluir o pedido #${pedido.id}?');">Excluir</a>
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
    SistemaCad &middot; Aula 07 &mdash; Pedido / ItemPedido vinculados ao CRUD de Clientes
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
