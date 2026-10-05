<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>LojavirtualInterface - Relatório de Vendas por Categoria</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<%@ include file="/WEB-INF/views/_nav.jspf" %>

<main class="container pb-5">

    <div class="mb-3">
        <h1 class="h3 mb-0">Relatório de Vendas por Categoria</h1>
        <p class="text-muted small mb-0">
            GET /relatorios/total-por-categoria na API lojavirtual &mdash; exercício da Aula 06,
            já devolvido pela própria API ordenado do maior total vendido para o menor.
        </p>
    </div>

    <c:if test="${not empty mensagemErro}">
        <div class="alert alert-danger">${mensagemErro}</div>
    </c:if>

    <div class="card">
        <div class="card-body">
            <c:choose>
                <c:when test="${empty relatorio}">
                    <p class="text-center text-muted py-4 mb-0">Nenhuma categoria cadastrada ainda.</p>
                </c:when>
                <c:otherwise>
                    <%-- Maior total da lista, para desenhar a barra proporcional de cada linha.
                         A lista já vem ordenada desc pela API, então é sempre o primeiro item. --%>
                    <c:set var="maiorTotal" value="${relatorio[0].totalVendido}"/>

                    <div class="table-responsive">
                        <table class="table align-middle mb-0">
                            <thead>
                                <tr>
                                    <th>Categoria</th>
                                    <th style="width:45%">Proporção</th>
                                    <th class="text-end">Total vendido</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="linha" items="${relatorio}">
                                    <tr>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/produtos?categoriaId=${linha.categoriaId}">
                                                ${linha.categoriaNome}
                                            </a>
                                        </td>
                                        <td>
                                            <c:set var="percentual" value="0"/>
                                            <c:if test="${maiorTotal > 0}">
                                                <c:set var="percentual" value="${(linha.totalVendido / maiorTotal) * 100}"/>
                                            </c:if>
                                            <div class="progress" style="height: 10px;">
                                                <div class="progress-bar" role="progressbar"
                                                     style="width: ${percentual}%;"
                                                     aria-valuenow="${percentual}" aria-valuemin="0" aria-valuemax="100">
                                                </div>
                                            </div>
                                        </td>
                                        <td class="text-end">
                                            R$ <fmt:formatNumber value="${linha.totalVendido}" pattern="0.00"/>
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
