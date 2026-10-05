async function buscarUsuarios() {
    const divUsuarios =
        document.getElementById("usuarios");
        divUsuarios.innerHTML = "<h2>Carregando usuários...</h2>";
    try {
        const resposta = await fetch("https://jsonplaceholder.typicode.com/users");
        const usuarios = await resposta.json();
        let html = "";
        usuarios.forEach(usuario => {
            html += `
                <div class="card">
                    <h3>${usuario.name}</h3>
                    <p>
                        <strong>Usuário:</strong>
                        ${usuario.username}
                    </p>
                    <p>
                        <strong>E-mail:</strong>
                        ${usuario.email}
                    </p>
                    <p>
                        <strong>Telefone:</strong>
                        ${usuario.phone}
                    </p>
                    <p>
                        <strong>Cidade:</strong>
                        ${usuario.address.city}
                    </p>
                    <p>
                        <strong>Empresa:</strong>
                        ${usuario.company.name}
                    </p>
                </div>
            `;
        });
        divUsuarios.innerHTML = html;
    } catch (erro) {
        divUsuarios.innerHTML =
            "<h2>Erro ao buscar dados da API.</h2>";
        console.error(erro);
    }
}
function limparDados() {
    document.getElementById("usuarios").innerHTML = "";
}