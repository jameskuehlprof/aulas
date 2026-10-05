let listaUsuarios = [];

async function buscarUsuarios() {

    const divUsuarios =
        document.getElementById("usuarios");

    divUsuarios.innerHTML =
        "<h2>Carregando usuários...</h2>";

    try {

        const resposta =
            await fetch(
                "https://jsonplaceholder.typicode.com/users"
            );

        listaUsuarios =
            await resposta.json();

        exibirUsuarios(listaUsuarios);

        document.getElementById("quantidade").innerHTML =
            `Total de usuários: ${listaUsuarios.length}`;

        const agora = new Date();

        document.getElementById("dataConsulta").innerHTML =
            `Consulta realizada em
            ${agora.toLocaleDateString()}
            às
            ${agora.toLocaleTimeString()}`;

        document.getElementById("jsonDados").value =
            JSON.stringify(listaUsuarios, null, 4);

    }
    catch(erro){

        divUsuarios.innerHTML =
            "<h2>Erro ao carregar usuários.</h2>";

        console.error(erro);
    }
}

function exibirUsuarios(usuarios){

    const divUsuarios =
        document.getElementById("usuarios");

    let html = "";

    usuarios.forEach(usuario => {

        html += `
            <div class="card">

                <img
                    src="https://robohash.org/${usuario.username}?size=120x120"
                    alt="${usuario.name}">

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
}

function filtrarUsuarios(){

    const texto =
        document.getElementById("pesquisa")
        .value
        .toLowerCase();

    const filtrados =
        listaUsuarios.filter(usuario =>
            usuario.name
            .toLowerCase()
            .includes(texto)
        );

    exibirUsuarios(filtrados);

    document.getElementById("quantidade").innerHTML =
        `Usuários encontrados: ${filtrados.length}`;
}