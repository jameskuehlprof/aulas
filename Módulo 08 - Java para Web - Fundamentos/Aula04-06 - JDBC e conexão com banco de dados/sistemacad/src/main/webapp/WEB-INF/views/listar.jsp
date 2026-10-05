<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>SistemaCad - Clientes</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <header class="topo">
        <h1>SistemaCad</h1>
        <p>Sistema de Cadastro de Clientes &mdash; Java Web + JDBC + MySQL</p>
    </header>

    <main class="container">

        <c:if test="${not empty mensagemErro}">
            <div class="alerta alerta-erro">${mensagemErro}</div>
        </c:if>

        <div class="card">
            <div class="barra-acoes">
                <h2 style="margin:0;">Clientes cadastrados</h2>
                <a class="btn btn-primario" href="${pageContext.request.contextPath}/clientes?acao=novo">
                    + Novo cliente
                </a>
            </div>

            <form class="busca-form" method="get"
                  action="${pageContext.request.contextPath}/clientes">
                <input type="hidden" name="acao" value="buscar">
                <input type="text" name="cidade" placeholder="Buscar por cidade..."
                       value="${filtroCidade}">
                <button class="btn btn-secundario" type="submit">Buscar</button>
                <c:if test="${not empty filtroCidade}">
                    <a class="btn btn-secundario"
                       href="${pageContext.request.contextPath}/clientes?acao=listar">Limpar</a>
                </c:if>
            </form>

            <c:choose>
                <c:when test="${empty clientes}">
                    <p class="alerta-vazio">Nenhum cliente encontrado.</p>
                </c:when>
                <c:otherwise>
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Nome</th>
                                <th>E-mail</th>
                                <th>Cidade</th>
                                <th>Ações</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="cliente" items="${clientes}">
                                <tr>
                                    <td><span class="badge">#${cliente.id}</span></td>
                                    <td>${cliente.nome}</td>
                                    <td>${cliente.email}</td>
                                    <td>${cliente.cidade}</td>
                                    <td class="acoes-linha">
                                        <a class="btn btn-secundario btn-sm"
                                           href="${pageContext.request.contextPath}/clientes?acao=editar&id=${cliente.id}">
                                           Editar
                                        </a>
                                        <a class="btn btn-perigo btn-sm"
                                           href="${pageContext.request.contextPath}/clientes?acao=excluir&id=${cliente.id}"
                                           onclick="return confirm('Excluir o cliente ' + '${cliente.nome}' + '?');">
                                           Excluir
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>
    </main>

    <footer class="rodape">SistemaCad &middot; Aula 4 &mdash; JDBC e Conexão com Banco de Dados</footer>

</body>
</html>
