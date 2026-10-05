function adicionarProduto() {
    // getElementById()
    let produto = document.getElementById("produto").value;
    let preco = document.getElementById("preco").value;
    if (produto.trim() === "" || preco === "") {
        alert("Preencha todos os campos!");
        return;
    }
    let lista = document.getElementById("lista");
    // innerHTML
    lista.innerHTML += `
        <li class="item-produto" data-preco="${preco}">
            ${produto} - R$ ${Number(preco).toFixed(2)}
        </li>
    `;
    document.getElementById("produto").value = "";
    document.getElementById("preco").value = "";
}

function destacarCaros() {
    // querySelectorAll()
    let produtos = document.querySelectorAll(".item-produto");
    produtos.forEach(function(item){
        let preco = Number(item.dataset.preco);
        if(preco >= 100){
            // classList
            item.classList.add("produto-caro");
        }
    });
}
function mudarCorLista(){
    // querySelector()
    let lista = document.querySelector("#lista");
    // style
    lista.style.backgroundColor = "#d1ecf1";
    lista.style.padding = "10px";
    lista.style.borderRadius = "8px";
}