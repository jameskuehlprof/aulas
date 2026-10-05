<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Produtos</title>
</head>
<body>
    <h1>Cadastro de Produtos</h1>
    <form action="resultado.jsp" method="post">
        <label>Nome do Produto:</label>
        <input type="text" name="nome" required>
        <br><br>
        <label>Preço:</label>
        <input type="number" name="preco" step="0.01">
        <br><br>
        <label>Quantidade:</label>
        <input type="number" name="quantidade">
        <br><br>
        <button type="submit">Cadastrar Produto</button>
    </form>
    <br>
    <a href="index.jsp">Voltar ao menu</a>
</body>
</html>