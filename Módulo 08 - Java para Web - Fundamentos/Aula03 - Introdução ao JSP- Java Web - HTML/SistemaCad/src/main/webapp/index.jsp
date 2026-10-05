<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>SistemaCad</title>

    <style>
        body {
            font-family: Arial, sans-serif;
            text-align: center;
            margin-top: 80px;
        }

        h1 {
            margin-bottom: 40px;
        }

        .menu {
            display: flex;
            justify-content: center;
            gap: 15px;
        }

        .menu a {
            text-decoration: none;
            background-color: #007bff;
            color: white;
            padding: 12px 25px;
            border-radius: 5px;
        }

        .menu a:hover {
            background-color: #0056b3;
        }
    </style>
</head>

<body>
    <h1>SISTEMA DE CADASTRO</h1>
    <div class="menu">
        <a href="cliente.jsp">Clientes</a>
        <a href="produto.jsp">Produtos</a>
        <a href="funcionario.jsp">Funcionários</a>
        <a href="cadastro.jsp">Cadastro</a>
    </div>
</body>
</html>