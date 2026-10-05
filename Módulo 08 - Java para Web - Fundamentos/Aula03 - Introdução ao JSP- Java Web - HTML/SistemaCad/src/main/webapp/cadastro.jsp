<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Cadastro</title>
</head>
<body>
    <h1>Cadastro de Pessoa</h1>
    <form action="resultado.jsp" method="post">
        <label>Nome:</label>
        <input type="text" name="nome" required>
        <br><br>
        <label>Idade:</label>
        <input type="number" name="idade">
        <br><br>
        <label>E-mail:</label>
        <input type="email" name="email">
        <br><br>
        <button type="submit">Cadastrar</button>
    </form>
    <br>
    <a href="index.jsp">Voltar ao início</a>
</body>
</html>