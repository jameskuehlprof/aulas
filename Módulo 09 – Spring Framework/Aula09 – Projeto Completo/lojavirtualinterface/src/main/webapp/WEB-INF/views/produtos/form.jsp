<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>LojavirtualInterface - ${empty produto.id ? 'Novo produto' : 'Editar produto'}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<%@ include file="/WEB-INF/views/_nav.jspf" %>

<main class="container pb-5" style="max-width: 640px;">

    <h1 class="h3 mb-3">${empty produto.id ? 'Novo produto' : 'Editar produto'}</h1>

    <c:if test="${not empty mensagemErro}">
        <div class="alert alert-danger">${mensagemErro}</div>
    </c:if>

    <div class="card">
        <div class="card-body">
            <form method="post" action="${pageContext.request.contextPath}/produtos">
                <input type="hidden" name="acao" value="salvar">
                <input type="hidden" name="id" value="${produto.id}">

                <div class="mb-3">
                    <label class="form-label" for="nome">Nome</label>
                    <input type="text" class="form-control" id="nome" name="nome"
                           maxlength="100" required value="${produto.nome}">
                </div>

                <div class="mb-3">
                    <label class="form-label" for="preco">Preço (R$)</label>
                    <input type="number" class="form-control" id="preco" name="preco"
                           min="0.01" step="0.01" required value="${produto.preco}">
                </div>

                <div class="mb-3">
                    <label class="form-label" for="categoriaId">Categoria</label>
                    <select class="form-select" id="categoriaId" name="categoriaId" required>
                        <option value="">Selecione...</option>
                        <c:forEach var="cat" items="${categorias}">
                            <option value="${cat.id}" ${cat.id == produto.categoriaId ? 'selected' : ''}>
                                ${cat.nome}
                            </option>
                        </c:forEach>
                    </select>
                    <c:if test="${empty categorias}">
                        <div class="form-text text-danger">
                            Nenhuma categoria cadastrada ainda — crie uma categoria antes de criar um produto.
                        </div>
                    </c:if>
                </div>

                <div class="d-flex gap-2">
                    <button class="btn btn-primary" type="submit">Salvar</button>
                    <a class="btn btn-outline-secondary"
                       href="${pageContext.request.contextPath}/produtos?acao=listar">Cancelar</a>
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
