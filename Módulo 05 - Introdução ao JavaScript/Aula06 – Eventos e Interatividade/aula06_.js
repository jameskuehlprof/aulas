// ==========================
// FORMULÁRIO COM VALIDAÇÃO
// ==========================

const formulario =
document.getElementById("formCadastro");

formulario.addEventListener("submit", function(event){

    event.preventDefault();

    let nome =
    document.getElementById("nome").value;

    let email =
    document.getElementById("email").value;

    let mensagem =
    document.getElementById("mensagem");

    if(nome.trim() === "" || email.trim() === ""){

        mensagem.textContent =
        "Preencha todos os campos.";

        mensagem.className = "erro";

        return;
    }

    // validação do @

    if(!email.includes("@")){

        mensagem.textContent =
        "O e-mail deve conter o caractere @";

        mensagem.className = "erro";

        return;
    }

    mensagem.textContent =
    "Cadastro realizado com sucesso!";

    mensagem.className = "sucesso";

});

// ==========================
// CONTADOR DE CARACTERES
// ==========================

const campoNome =
document.getElementById("nome");

const contadorCaracteres =
document.getElementById("contadorCaracteres");

campoNome.addEventListener("keydown", function(){

    setTimeout(function(){

        contadorCaracteres.textContent =
        "Caracteres digitados: " +
        campoNome.value.length;

    }, 0);

});

// ==========================
// BOTÃO LIMPAR
// ==========================

document
.getElementById("btnLimpar")
.addEventListener("click", function(){

    let confirmar =
    confirm("Deseja realmente limpar os campos?");

    if(confirmar){

        document.getElementById("nome").value = "";
        document.getElementById("email").value = "";

        contadorCaracteres.textContent =
        "Caracteres digitados: 0";

        document.getElementById("mensagem")
        .textContent = "";
    }

});

// ==========================
// CONTADOR INTERATIVO
// ==========================

let valor = 0;

const contador =
document.getElementById("contador");

document
.getElementById("btnMais")
.addEventListener("click", function(){

    valor++;
    contador.textContent = valor;

});

document
.getElementById("btnMenos")
.addEventListener("click", function(){

    valor--;
    contador.textContent = valor;

});

// ==========================
// BOTÃO ZERAR
// ==========================

document
.getElementById("btnZerar")
.addEventListener("click", function(){

    valor = 0;

    contador.textContent = valor;

});

// ==========================
// TEMA ESCURO
// ==========================

document
.getElementById("btnTema")
.addEventListener("click", function(){

    document.body.classList.remove(
        "tema-colorido"
    );

    document.body.classList.toggle(
        "tema-escuro"
    );

});

// ==========================
// TEMA COLORIDO
// ==========================

document
.getElementById("btnTemaColorido")
.addEventListener("click", function(){

    document.body.classList.remove(
        "tema-escuro"
    );

    document.body.classList.toggle(
        "tema-colorido"
    );

});

// ==========================
// MOUSEOVER
// ==========================

const caixa =
document.getElementById("caixa");

caixa.addEventListener("mouseover", function(){

    caixa.style.backgroundColor =
    "#ffc107";

});

caixa.addEventListener("mouseout", function(){

    caixa.style.backgroundColor =
    "#ddd";

});

// ==========================
// KEYDOWN
// ==========================

const teclado =
document.getElementById("teclado");

const saida =
document.getElementById("saidaTecla");

teclado.addEventListener("keydown", function(event){

    saida.textContent =
    "Tecla pressionada: " + event.key;

});