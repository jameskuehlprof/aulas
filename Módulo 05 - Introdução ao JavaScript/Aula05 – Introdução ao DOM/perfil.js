function atualizarPerfil() {
    let nome = document.getElementById("nome").value;
    let profissao = document.getElementById("profissao").value;
    let bio = document.getElementById("bio").value;
    if (
        nome.trim() === "" ||
        profissao.trim() === "" ||
        bio.trim() === ""
    ) {
        alert("Preencha todos os campos!");
        return;
    }
    document.getElementById("nomePerfil").textContent = nome;
    document.getElementById("profissaoPerfil").textContent = profissao;
    document.getElementById("bioPerfil").textContent = bio;
}