<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Clientes</title>
</head>
<body>
    <h1>Cadastro de Clientes</h1>
    <form action="resultado.jsp" method="post">
        <label>Nome do Cliente:</label>
        <input type="text" name="nome" required>
        <br><br>
        <label>CPF:</label>
        <input type="text" name="cpf">
        <br><br>
        <label>E-mail:</label>
        <input type="email" name="email">
        <br><br>
        <label>Cidade:</label>
        <input type="text" name="cidade">
        <br><br>
        <button type="submit">Cadastrar Cliente</button>
    </form>
    <br>
    <a href="index.jsp">Voltar ao menu</a>
</body>
</html>