<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>SistemaCad - ${empty cliente.id or cliente.id == 0 ? 'Novo cliente' : 'Editar cliente'}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <header class="topo">
        <h1>SistemaCad</h1>
        <p>Sistema de Cadastro de Clientes &mdash; Java Web + JDBC + MySQL</p>
    </header>

    <main class="container">

        <div class="card">
            <h2>${empty cliente.id or cliente.id == 0 ? 'Novo cliente' : 'Editar cliente'}</h2>

            <c:if test="${not empty mensagemErro}">
                <div class="alerta alerta-erro">${mensagemErro}</div>
            </c:if>

            <form class="formulario" method="post"
                  action="${pageContext.request.contextPath}/clientes">
                <input type="hidden" name="acao" value="salvar">
                <input type="hidden" name="id" value="${cliente.id}">

                <div class="campo">
                    <label for="nome">Nome</label>
                    <input type="text" id="nome" name="nome" maxlength="100" required
                           value="${cliente.nome}">
                </div>

                <div class="campo">
                    <label for="email">E-mail</label>
                    <input type="email" id="email" name="email" maxlength="150" required
                           value="${cliente.email}">
                </div>

                <div class="campo">
                    <label for="cidade">Cidade</label>
                    <input type="text" id="cidade" name="cidade" maxlength="100"
                           value="${cliente.cidade}">
                </div>

                <div class="acoes-form">
                    <button class="btn btn-primario" type="submit">Salvar</button>
                    <a class="btn btn-secundario"
                       href="${pageContext.request.contextPath}/clientes?acao=listar">Cancelar</a>
                </div>
            </form>
        </div>
    </main>

    <footer class="rodape">SistemaCad &middot; Aula 4 &mdash; JDBC e Conexão com Banco de Dados</footer>

</body>
</html>
