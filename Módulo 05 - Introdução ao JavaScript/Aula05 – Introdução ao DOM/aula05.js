// getElementById() + textContent
function alterarTexto(){
    let nome = document.getElementById("nome").value;
    if(nome.trim() === ""){
        alert("Digite um nome!");
        return;
    }
    document.getElementById("mensagem").textContent =
        "Olá, " + nome + "! Bem-vindo ao DOM.";
}
// innerHTML
function criarConteudo(){
    let conteudo = document.getElementById("conteudo");
    conteudo.innerHTML =
    `
        <h3>Conteúdo criado dinamicamente</h3>
        <p>Este conteúdo foi inserido usando innerHTML.</p>
    `;
}
// querySelector() + style
function mudarCor(){
    let caixa = document.querySelector("#caixa");
    caixa.style.backgroundColor = "#198754";
    caixa.style.color = "white";
    caixa.style.fontSize = "22px";
}
// classList
function alternarClasse(){
    let caixa = document.querySelector("#caixa");
    caixa.classList.toggle("destaque");

}
// querySelectorAll()
function destacarItens(){
    let itens = document.querySelectorAll(".item");
    itens.forEach(function(item){
        item.classList.add("item-destaque");
    });
}