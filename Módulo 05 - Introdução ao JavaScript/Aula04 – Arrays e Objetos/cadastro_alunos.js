let alunos = [];

function cadastrar() {

    let nome = document.getElementById("nome").value;
    let idade = Number(document.getElementById("idade").value);
    let curso = document.getElementById("curso").value;
    let nota = Number(document.getElementById("nota").value);

    if(nome === "" || curso === "" || isNaN(idade) || isNaN(nota)){
        alert("Preencha todos os campos!");
        return;
    }

    let situacao = "";

    if(nota >= 7){
        situacao = "Aprovado";
    }
    else if(nota >= 5){
        situacao = "Recuperação";
    }
    else{
        situacao = "Reprovado";
    }

    let aluno = {
        nome: nome,
        idade: idade,
        curso: curso,
        nota: nota,
        situacao: situacao
    };

    alunos.push(aluno);

    exibirAlunos();

    limparCampos();
}

function exibirAlunos() {

    let resultado = document.getElementById("resultado");

    resultado.innerHTML = `
        <table>
            <thead>
                <tr>
                    <th>Nome</th>
                    <th>Idade</th>
                    <th>Curso</th>
                    <th>Nota</th>
                    <th>Situação</th>
                </tr>
            </thead>
            <tbody id="tabelaAlunos">
            </tbody>
        </table>
    `;

    let tabela = document.getElementById("tabelaAlunos");

    alunos.forEach((aluno) => {

        let classe = "";

        if(aluno.situacao === "Aprovado"){
            classe = "aprovado";
        }
        else if(aluno.situacao === "Recuperação"){
            classe = "recuperacao";
        }
        else{
            classe = "reprovado";
        }

        tabela.innerHTML += `
            <tr>
                <td>${aluno.nome}</td>
                <td>${aluno.idade}</td>
                <td>${aluno.curso}</td>
                <td>${aluno.nota}</td>
                <td class="${classe}">
                    ${aluno.situacao}
                </td>
            </tr>
        `;
    });

}

function limparCampos(){
    document.getElementById("nome").value = "";
    document.getElementById("idade").value = "";
    document.getElementById("curso").value = "";
    document.getElementById("nota").value = "";

    document.getElementById("nome").focus();
}