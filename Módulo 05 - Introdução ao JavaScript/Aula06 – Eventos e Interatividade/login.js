// Capturando elementos
const form = document.getElementById("formLogin");
const usuario = document.getElementById("usuario");
const senha = document.getElementById("senha");
const btnLogin = document.getElementById("btnLogin");
const mensagem = document.getElementById("mensagem");

// Evento keydown
usuario.addEventListener("keydown", function(event){
    console.log("Tecla pressionada no campo usuário:", event.key);
});

senha.addEventListener("keydown", function(event){
    console.log("Tecla pressionada no campo senha:", event.key);
});

// Evento click
btnLogin.addEventListener("click", function(){
    console.log("Botão Entrar clicado");
});

// Evento submit
form.addEventListener("submit", function(event){

    // Impede recarregar a página
    event.preventDefault();

    const usuarioCorreto = "admin";
    const senhaCorreta = "1234";

    if(usuario.value === usuarioCorreto && senha.value === senhaCorreta){

        mensagem.textContent = "Login realizado com sucesso!";
        mensagem.className = "sucesso";

    }else{

        mensagem.textContent = "Usuário ou senha inválidos!";
        mensagem.className = "erro";

    }

});