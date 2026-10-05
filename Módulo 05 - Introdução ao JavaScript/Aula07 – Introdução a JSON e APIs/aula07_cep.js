async function buscarCEP() {
    const cep = document.getElementById("cep").value;
    const resultado = document.getElementById("resultado");
    const dataConsulta = document.getElementById("dataConsulta");
    const jsonDados = document.getElementById("jsonDados");
    if (cep.length !== 8) {
        alert("Digite um CEP válido com 8 números.");
        return;
    }
    resultado.innerHTML = "Consultando CEP...";
    try {
        const resposta = await fetch(`https://viacep.com.br/ws/${cep}/json/`);
        const dados = await resposta.json();

        if (dados.erro) {
            resultado.innerHTML =
                "<p>CEP não encontrado.</p>";
            jsonDados.value = "";
            return;
        }
        resultado.innerHTML = ` <h2>Endereço Encontrado</h2>
            <p><strong>CEP:</strong> ${dados.cep}</p>
            <p><strong>Logradouro:</strong> ${dados.logradouro}</p>
            <p><strong>Bairro:</strong> ${dados.bairro}</p>
            <p><strong>Cidade:</strong> ${dados.localidade}</p>
            <p><strong>Estado:</strong> ${dados.uf}</p>
            <p><strong>DDD:</strong> ${dados.ddd}</p>
        `;
        const agora = new Date();
        dataConsulta.innerHTML =
            `Consulta realizada em:
            ${agora.toLocaleDateString()}
            às
            ${agora.toLocaleTimeString()}`;
        jsonDados.value =
            JSON.stringify(dados, null, 4);
    }
    catch (erro) {
        resultado.innerHTML = "<p>Erro ao consultar a API.</p>";
        console.error(erro);
    }
}