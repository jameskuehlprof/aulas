<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>LojavirtualInterface</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<%@ include file="/WEB-INF/views/_nav.jspf" %>

<main class="container pb-5">

    <div class="mb-4">
        <h1 class="h3">Interface gráfica do lojavirtual</h1>
        <p class="text-muted">
            Este projeto é Java Web "puro" (Servlet + JSP + JSTL, o mesmo estilo do
            <strong>sistemacad</strong> no Módulo 08) — mas em vez de um DAO conversando
            direto com o banco via JDBC, cada Servlet chama a
            <strong>API REST do lojavirtual</strong> (projeto Spring Boot do Módulo 09,
            pasta irmã desta) através da classe <code>client.ApiClient</code>.
        </p>
        <pre class="bg-light border rounded p-3 small mb-0" style="white-space: pre-wrap;">Navegador → JSP → Servlet → ApiClient (HTTP/JSON) → API lojavirtual (Spring Boot, porta 8080) → MySQL</pre>
    </div>

    <div class="row g-3">
        <div class="col-md-4 col-sm-6">
            <a class="text-decoration-none text-reset" href="${pageContext.request.contextPath}/categorias">
                <div class="card card-recurso">
                    <div class="card-body">
                        <h2 class="h5">Categorias</h2>
                        <p class="text-muted small mb-0">CRUD completo — GET/POST/PUT/DELETE /categorias</p>
                    </div>
                </div>
            </a>
        </div>
        <div class="col-md-4 col-sm-6">
            <a class="text-decoration-none text-reset" href="${pageContext.request.contextPath}/produtos">
                <div class="card card-recurso">
                    <div class="card-body">
                        <h2 class="h5">Produtos</h2>
                        <p class="text-muted small mb-0">CRUD completo, com combo de categoria — /produtos</p>
                    </div>
                </div>
            </a>
        </div>
        <div class="col-md-4 col-sm-6">
            <a class="text-decoration-none text-reset" href="${pageContext.request.contextPath}/clientes">
                <div class="card card-recurso">
                    <div class="card-body">
                        <h2 class="h5">Clientes</h2>
                        <p class="text-muted small mb-0">CRUD completo — GET/POST/PUT/DELETE /clientes</p>
                    </div>
                </div>
            </a>
        </div>
        <div class="col-md-4 col-sm-6">
            <a class="text-decoration-none text-reset" href="${pageContext.request.contextPath}/pedidos">
                <div class="card card-recurso">
                    <div class="card-body">
                        <h2 class="h5">Pedidos</h2>
                        <p class="text-muted small mb-0">
                            Criação combinando cliente + itens de produtos reais — sem PUT
                            (a API não permite alterar pedido, só cancelar/excluir).
                        </p>
                    </div>
                </div>
            </a>
        </div>
        <div class="col-md-4 col-sm-6">
            <a class="text-decoration-none text-reset" href="${pageContext.request.contextPath}/relatorios">
                <div class="card card-recurso">
                    <div class="card-body">
                        <h2 class="h5">Relatorio</h2>
                        <p class="text-muted small mb-0">Total vendido por categoria — exercício da Aula 6</p>
                    </div>
                </div>
            </a>
        </div>
        <div class="col-md-4 col-sm-6">
            <div class="card card-recurso border-0 bg-transparent shadow-none">
                <div class="card-body">
                    <h2 class="h5 text-muted">Swagger &amp; Postman</h2>
                    <p class="text-muted small mb-0">
                        A API continua exposta normalmente em
                        <code>http://localhost:8080/swagger-ui.html</code> e pela collection do
                        Postman — esta tela é só mais um cliente dela, não substitui os outros.
                    </p>
                </div>
            </div>
        </div>
    </div>
</main>

<footer class="text-center text-muted small py-4">
    LojavirtualInterface &middot; Aula 09 &mdash; Java para Web (Módulo 08) consumindo a API Spring Boot (Módulo 09)
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
