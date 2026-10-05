function converter() {
    const aluno = {
        nome: "João",
        curso: "JavaScript"
    };
    const texto = JSON.stringify(aluno);
    const objeto = JSON.parse(texto);
    document.getElementById("resultado").innerHTML = objeto.nome + " - " + objeto.curso;
}
