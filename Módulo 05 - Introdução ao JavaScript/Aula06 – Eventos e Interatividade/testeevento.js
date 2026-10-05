const btn = document.getElementById("btn");
const texto = document.getElementById("texto");

btn.addEventListener("click", function() {
    texto.textContent = "Você clicou no botão!";
});

let valor = 0;

document.getElementById("somar")
.addEventListener("click", function() {
    valor++;
    document.getElementById("contador")
    .textContent = valor;
});

const caixa = document.getElementById("caixa");
caixa.addEventListener("mouseover", () => {
    caixa.style.background = "orange";
});

caixa.addEventListener("mouseout", () => {
    caixa.style.background = "#ccc";
});

const nome = document.getElementById("nome");

nome.addEventListener("keydown", function() {
    console.log("Tecla pressionada");
});

document.addEventListener("keydown", function(event){
    alert("Você pressionou: " + event.key);
});