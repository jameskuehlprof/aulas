<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>SistemaCad - ${empty pedido.id or pedido.id == 0 ? 'Novo pedido' : 'Editar pedido'}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<%@ include file="/WEB-INF/views/_nav.jspf" %>

<main class="container pb-5" style="max-width: 860px;">

    <h1 class="h3 mb-3">${empty pedido.id or pedido.id == 0 ? 'Novo pedido' : 'Editar pedido'}</h1>

    <c:if test="${not empty mensagemErro}">
        <div class="alert alert-danger">${mensagemErro}</div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/pedidos" id="formPedido" novalidate>
        <input type="hidden" name="acao" value="salvar">
        <input type="hidden" name="id" value="${pedido.id}">

        <div class="card mb-3">
            <div class="card-body">
                <div class="row g-3">
                    <div class="col-md-6">
                        <label class="form-label" for="clienteId">Cliente</label>
                        <select class="form-select" id="clienteId" name="clienteId" required>
                            <option value="">Selecione...</option>
                            <c:forEach var="cli" items="${clientes}">
                                <option value="${cli.id}" ${cli.id == pedido.clienteId ? 'selected' : ''}>
                                    ${cli.nome}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-3">
                        <label class="form-label" for="dataPedido">Data do pedido</label>
                        <input type="date" class="form-control" id="dataPedido" name="dataPedido"
                               value="${pedido.dataPedido}" required>
                    </div>
                    <div class="col-md-3">
                        <label class="form-label" for="status">Status</label>
                        <select class="form-select" id="status" name="status">
                            <c:forEach var="s" items="${statusValidos}">
                                <option value="${s}" ${s == pedido.status ? 'selected' : ''}>${s}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
            </div>
        </div>

        <div class="card mb-3">
            <div class="card-body">
                <div class="d-flex justify-content-between align-items-center mb-2">
                    <h2 class="h5 mb-0">Itens do pedido</h2>
                    <button type="button" class="btn btn-sm btn-outline-primary" id="btnAdicionarItem">
                        + Adicionar item
                    </button>
                </div>

                <div class="table-responsive">
                    <table class="table align-middle" id="tabelaItens">
                        <thead>
                            <tr>
                                <th style="width:40%">Produto</th>
                                <th style="width:15%">Quantidade</th>
                                <th style="width:20%">Preço unit. (R$)</th>
                                <th style="width:15%" class="text-end">Subtotal (R$)</th>
                                <th style="width:10%"></th>
                            </tr>
                        </thead>
                        <tbody id="corpoItens">
                            <c:forEach var="item" items="${pedido.itens}">
                                <tr class="item-pedido-linha">
                                    <td>
                                        <input type="text" class="form-control" name="produto[]"
                                               maxlength="100" required value="${item.produto}">
                                    </td>
                                    <td>
                                        <input type="number" class="form-control campo-quantidade"
                                               name="quantidade[]" min="1" step="1" required
                                               value="${item.quantidade}">
                                    </td>
                                    <td>
                                        <input type="number" class="form-control campo-preco"
                                               name="precoUnitario[]" min="0" step="0.01" required
                                               value="${item.precoUnitario}">
                                    </td>
                                    <td class="text-end subtotal-linha">0,00</td>
                                    <td class="text-end">
                                        <button type="button" class="btn btn-sm btn-outline-danger btn-remover-item">&times;</button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

                <p class="text-muted small mb-0" id="avisoSemItens" style="display:none;">
                    Adicione ao menos um item ao pedido.
                </p>

                <div class="text-end mt-2">
                    <span class="fs-5">Total: R$ <strong id="totalPedido">0,00</strong></span>
                </div>
            </div>
        </div>

        <div class="d-flex gap-2">
            <button class="btn btn-primary" type="submit">Salvar</button>
            <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/pedidos?acao=listar">Cancelar</a>
        </div>
    </form>
</main>

<footer class="text-center text-muted small py-4">
    SistemaCad &middot; Aula 07 &mdash; Pedido / ItemPedido vinculados ao CRUD de Clientes
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
(function () {
    "use strict";

    // Este bloco só cuida da EXPERIÊNCIA no navegador: permitir adicionar
    // e remover linhas de item, e mostrar um total calculado ao vivo.
    // A validação e o cálculo definitivo do total sempre são refeitos
    // no servidor (PedidoServlet + PedidoDAO) — nunca confiamos apenas
    // no que o JavaScript calculou.

    var corpoItens = document.getElementById("corpoItens");
    var btnAdicionarItem = document.getElementById("btnAdicionarItem");
    var totalPedidoEl = document.getElementById("totalPedido");
    var avisoSemItens = document.getElementById("avisoSemItens");

    function criarLinha() {
        var tr = document.createElement("tr");
        tr.className = "item-pedido-linha";
        tr.innerHTML =
            '<td><input type="text" class="form-control" name="produto[]" maxlength="100" required></td>' +
            '<td><input type="number" class="form-control campo-quantidade" name="quantidade[]" min="1" step="1" required value="1"></td>' +
            '<td><input type="number" class="form-control campo-preco" name="precoUnitario[]" min="0" step="0.01" required value="0.00"></td>' +
            '<td class="text-end subtotal-linha">0,00</td>' +
            '<td class="text-end"><button type="button" class="btn btn-sm btn-outline-danger btn-remover-item">&times;</button></td>';
        return tr;
    }

    function formatarMoeda(valor) {
        return valor.toLocaleString("pt-BR", { minimumFractionDigits: 2, maximumFractionDigits: 2 });
    }

    function recalcular() {
        var linhas = corpoItens.querySelectorAll(".item-pedido-linha");
        var total = 0;

        linhas.forEach(function (linha) {
            var quantidade = parseFloat(linha.querySelector(".campo-quantidade").value) || 0;
            var preco = parseFloat(linha.querySelector(".campo-preco").value) || 0;
            var subtotal = quantidade * preco;

            linha.querySelector(".subtotal-linha").textContent = formatarMoeda(subtotal);
            total += subtotal;
        });

        totalPedidoEl.textContent = formatarMoeda(total);
        avisoSemItens.style.display = (linhas.length === 0) ? "block" : "none";
    }

    btnAdicionarItem.addEventListener("click", function () {
        corpoItens.appendChild(criarLinha());
        recalcular();
    });

    corpoItens.addEventListener("click", function (evento) {
        if (evento.target.classList.contains("btn-remover-item")) {
            evento.target.closest("tr").remove();
            recalcular();
        }
    });

    corpoItens.addEventListener("input", function (evento) {
        if (evento.target.classList.contains("campo-quantidade") ||
            evento.target.classList.contains("campo-preco")) {
            recalcular();
        }
    });

    document.getElementById("formPedido").addEventListener("submit", function (evento) {
        if (corpoItens.querySelectorAll(".item-pedido-linha").length === 0) {
            evento.preventDefault();
            avisoSemItens.style.display = "block";
        }
    });

    // Se o pedido ainda não tem nenhum item (caso "novo pedido"), já
    // começa com uma linha em branco para facilitar o preenchimento.
    if (corpoItens.querySelectorAll(".item-pedido-linha").length === 0) {
        corpoItens.appendChild(criarLinha());
    }

    recalcular();
})();
</script>
</body>
</html>
