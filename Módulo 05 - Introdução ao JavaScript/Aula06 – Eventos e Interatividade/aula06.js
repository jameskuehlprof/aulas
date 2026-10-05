// FORMULÁRIO COM VALIDAÇÃO
const formulario = document.getElementById("formCadastro");
//let resultado = document.getElementById("resultado");

formulario.addEventListener("submit", function (event) {
    event.preventDefault();

    let nome = document.getElementById("nome").value;
    let email = document.getElementById("email").value;
    let mensagem = document.getElementById("mensagem");

    if (nome.trim() === "" || email.trim() === "") {
        mensagem.textContent = "Preencha todos os campos.";
        mensagem.className = "erro";
        return;
    }
    mensagem.textContent = "Cadastro realizado com sucesso!";
    mensagem.className = "sucesso";
   /* resultado.innerHTML = 
    `<h3>Resultado: ${mensagem.textContent}</h3> <br> 
    `;*/
});

// CONTADOR INTERATIVO
let valor = 0;
const contador = document.getElementById("contador");
document
    .getElementById("btnMais")
    .addEventListener("click", function () {
        valor++;
        contador.textContent = valor;
    });
document
    .getElementById("btnMenos")
    .addEventListener("click", function () {
        valor--;
        contador.textContent = valor;
    });

// TROCA DE TEMA
document
    .getElementById("btnTema")
    .addEventListener("click", function () {
        document.body.classList.toggle(
            "tema-escuro"
        );
    });

// MOUSEOVER
const caixa = document.getElementById("caixa");
caixa.addEventListener("mouseover", function () {
    caixa.style.backgroundColor = "#ffc107";
});

caixa.addEventListener("mouseout", function () {
    caixa.style.backgroundColor = "#ddd";
});

// KEYDOWN
const teclado = document.getElementById("teclado");
const saida = document.getElementById("saidaTecla");
teclado.addEventListener("keydown", function (event) {
saida.textContent = "Tecla pressionada: " + event.key;
});

