<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>LojavirtualInterface - Novo pedido</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<%@ include file="/WEB-INF/views/_nav.jspf" %>

<main class="container pb-5" style="max-width: 860px;">

    <h1 class="h3 mb-3">Novo pedido</h1>
    <p class="text-muted small mb-3">
        POST /pedidos na API lojavirtual. Não existe "editar pedido" aqui: a API não tem PUT em /pedidos
        &mdash; um pedido é auditável, então o fluxo correto é excluir (DELETE) e criar um novo.
    </p>

    <c:if test="${not empty mensagemErro}">
        <div class="alert alert-danger">${mensagemErro}</div>
    </c:if>

    <c:if test="${empty produtos}">
        <div class="alert alert-warning">
            Nenhum produto cadastrado ainda &mdash; crie ao menos um produto antes de montar um pedido.
        </div>
    </c:if>

    <c:if test="${empty clientes}">
        <div class="alert alert-warning">
            Nenhum cliente cadastrado ainda &mdash; crie ao menos um cliente antes de montar um pedido.
        </div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/pedidos" id="formPedido" novalidate>
        <input type="hidden" name="acao" value="salvar">

        <div class="card mb-3">
            <div class="card-body">
                <label class="form-label" for="clienteId">Cliente</label>
                <select class="form-select" id="clienteId" name="clienteId" required>
                    <option value="">Selecione...</option>
                    <c:forEach var="cli" items="${clientes}">
                        <option value="${cli.id}" ${cli.id == clienteIdPreSelecionado ? 'selected' : ''}>
                            ${cli.nome}
                        </option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <div class="card mb-3">
            <div class="card-body">
                <div class="d-flex justify-content-between align-items-center mb-2">
                    <h2 class="h5 mb-0">Itens do pedido</h2>
                    <button type="button" class="btn btn-sm btn-outline-primary" id="btnAdicionarItem"
                            ${empty produtos ? 'disabled' : ''}>
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
                            <%-- As linhas são sempre montadas via JavaScript (criarLinha()), inclusive a
                                 primeira, para que o <select> de produto e o preço em PRODUTOS fiquem
                                 sempre em um único lugar. Veja o bloco <script> logo abaixo. --%>
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
            <button class="btn btn-primary" type="submit" ${empty produtos ? 'disabled' : ''}>Salvar</button>
            <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/pedidos?acao=listar">Cancelar</a>
        </div>
    </form>
</main>

<footer class="text-center text-muted small py-4">
    LojavirtualInterface &middot; Aula 09 &mdash; interface gráfica para a API lojavirtual
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
(function () {
    "use strict";

    // Este bloco só cuida da EXPERIÊNCIA no navegador: montar as linhas de
    // item, deixar escolher o produto num <select> e mostrar preço unitário
    // e total calculados ao vivo, usando os preços atuais de ${produtos}.
    // O valor definitivo NUNCA vem do JavaScript: o Servlet só envia
    // produtoId + quantidade para a API (ver ItemPedidoRequestDTO), e é a
    // própria API quem busca o preço atual do produto e "congela" o
    // precoUnitario do item no momento da criação do pedido.

    var PRODUTOS = {
        <c:forEach var="produto" items="${produtos}" varStatus="status">
        "${produto.id}": { nome: "${produto.nome}", preco: <fmt:formatNumber value="${produto.preco}" pattern="0.00"/> }<c:if test="${!status.last}">,</c:if>
        </c:forEach>
    };

    var corpoItens = document.getElementById("corpoItens");
    var btnAdicionarItem = document.getElementById("btnAdicionarItem");
    var totalPedidoEl = document.getElementById("totalPedido");
    var avisoSemItens = document.getElementById("avisoSemItens");

    function opcoesProduto(selecionadoId) {
        var html = '<option value="">Selecione...</option>';
        Object.keys(PRODUTOS).forEach(function (id) {
            var selecionado = (String(selecionadoId) === id) ? " selected" : "";
            html += '<option value="' + id + '"' + selecionado + '>' + PRODUTOS[id].nome + '</option>';
        });
        return html;
    }

    function criarLinha() {
        var tr = document.createElement("tr");
        tr.className = "item-pedido-linha";
        tr.innerHTML =
            '<td><select class="form-select campo-produto" name="produtoId[]" required>' + opcoesProduto("") + '</select></td>' +
            '<td><input type="number" class="form-control campo-quantidade" name="quantidade[]" min="1" step="1" required value="1"></td>' +
            '<td class="campo-preco-exibicao text-muted">0,00</td>' +
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
            var produtoId = linha.querySelector(".campo-produto").value;
            var quantidade = parseFloat(linha.querySelector(".campo-quantidade").value) || 0;
            var produto = PRODUTOS[produtoId];
            var preco = produto ? produto.preco : 0;
            var subtotal = quantidade * preco;

            linha.querySelector(".campo-preco-exibicao").textContent = formatarMoeda(preco);
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
        if (evento.target.classList.contains("campo-quantidade")) {
            recalcular();
        }
    });

    corpoItens.addEventListener("change", function (evento) {
        if (evento.target.classList.contains("campo-produto")) {
            recalcular();
        }
    });

    document.getElementById("formPedido").addEventListener("submit", function (evento) {
        if (corpoItens.querySelectorAll(".item-pedido-linha").length === 0) {
            evento.preventDefault();
            avisoSemItens.style.display = "block";
        }
    });

    // Começa com uma linha em branco para facilitar o preenchimento,
    // desde que exista pelo menos um produto cadastrado.
    if (Object.keys(PRODUTOS).length > 0) {
        corpoItens.appendChild(criarLinha());
    }

    recalcular();
})();
</script>
</body>
</html>
