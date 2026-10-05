<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>LojavirtualInterface - Pedidos</title>
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
                GET/POST/DELETE /pedidos na API lojavirtual &mdash; sem PUT: pedido não se edita, só se exclui
                <c:if test="${not empty clienteFiltro}">
                    &mdash; cliente: <strong>${clienteFiltro.nome}</strong>
                </c:if>
            </p>
        </div>
        <div class="d-flex gap-2">
            <c:if test="${not empty clienteFiltro}">
                <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/pedidos">Ver todos</a>
            </c:if>
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
                                    <th style="width:2rem"></th>
                                    <th>ID</th>
                                    <th>Cliente</th>
                                    <th>Data</th>
                                    <th class="text-end">Itens</th>
                                    <th class="text-end">Total</th>
                                    <th class="text-end">Ações</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="pedido" items="${pedidos}">
                                    <tr data-bs-toggle="collapse" data-bs-target="#itens${pedido.id}"
                                        role="button" style="cursor:pointer;">
                                        <td class="text-muted">&#9662;</td>
                                        <td><span class="badge text-bg-light border">#${pedido.id}</span></td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/pedidos?clienteId=${pedido.clienteId}"
                                               onclick="event.stopPropagation();">
                                                ${pedido.clienteNome}
                                            </a>
                                        </td>
                                        <td>${pedido.dataPedido}</td>
                                        <td class="text-end">${pedido.itens.size()}</td>
                                        <td class="text-end">R$ <fmt:formatNumber value="${pedido.total}" pattern="0.00"/></td>
                                        <td class="text-end">
                                            <a class="btn btn-sm btn-outline-danger"
                                               href="${pageContext.request.contextPath}/pedidos?acao=excluir&id=${pedido.id}"
                                               onclick="event.stopPropagation(); return confirm('Excluir o pedido #${pedido.id}?');">Excluir</a>
                                        </td>
                                    </tr>
                                    <tr class="collapse" id="itens${pedido.id}">
                                        <td></td>
                                        <td colspan="6" class="pb-3">
                                            <table class="table table-sm mb-0">
                                                <thead>
                                                    <tr class="text-muted small">
                                                        <th>Produto</th>
                                                        <th class="text-end">Quantidade</th>
                                                        <th class="text-end">Preço unit.</th>
                                                        <th class="text-end">Subtotal</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="item" items="${pedido.itens}">
                                                        <tr>
                                                            <td>${item.produtoNome}</td>
                                                            <td class="text-end">${item.quantidade}</td>
                                                            <td class="text-end">R$ <fmt:formatNumber value="${item.precoUnitario}" pattern="0.00"/></td>
                                                            <td class="text-end">R$ <fmt:formatNumber value="${item.subtotal}" pattern="0.00"/></td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
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
