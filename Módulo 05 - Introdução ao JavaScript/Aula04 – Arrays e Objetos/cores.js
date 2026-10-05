function mostrarCores() {

    // Array inicial
    let cores = ["Azul", "Verde", "Amarelo"];

    let resultado = document.getElementById("resultado");

    resultado.innerHTML = `
        <h3>Array Inicial</h3>
        <p>${cores.join(", ")}</p>

        <p><strong>Quantidade de cores:</strong> ${cores.length}</p>
    `;

    // Adiciona Vermelho
    cores.push("Vermelho");

    resultado.innerHTML += `
        <h3>Após adicionar Vermelho</h3>
        <p>${cores.join(", ")}</p>
    `;

    // Remove o último item
    cores.pop();

    resultado.innerHTML += `
        <h3>Após remover o último item</h3>
        <p>${cores.join(", ")}</p>

        <p><strong>Quantidade final:</strong> ${cores.length}</p>
    `;
}