<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Funcionários</title>
</head>
<body>
    <h1>Cadastro de Funcionários</h1>
    <form action="resultado.jsp" method="post">
        <label>Nome:</label>
        <input type="text" name="nome" required>
        <br><br>
        <label>Cargo:</label>
        <input type="text" name="cargo">
        <br><br>
        <label>Salário:</label>
        <input type="number" name="salario" step="0.01">
        <br><br>
        <button type="submit">Cadastrar Funcionário</button>
    </form>
    <br>
    <a href="index.jsp">Voltar ao menu</a>
</body>
</html>