// Variável global
let alunos = [];

/*
Função para verificar idade
Retorna a situação do aluno
*/
function verificarIdade(idade){

    if(idade >= 18){
        return "Maior de idade";
    }

    return "Menor de idade";
}

/*
Função para calcular média
*/
function calcularMedia(soma, quantidade){

    return soma / quantidade;
}

/*
Função principal
*/
function cadastrarAlunos(){

    alunos = [];

    let quantidade =
    Number(document.getElementById("quantidade").value);

    if(quantidade <= 0){

        alert("Informe uma quantidade válida!");
        return;
    }

    let somaIdades = 0;
    let maiores = 0;
    let menores = 0;

    // Estrutura de repetição FOR
    for(let i = 1; i <= quantidade; i++){

        let nome =
        prompt(`Digite o nome do aluno ${i}:`);

        let idade =
        Number(prompt(`Digite a idade de ${nome}:`));

        let situacao =
        verificarIdade(idade);

        if(idade >= 18){
            maiores++;
        }else{
            menores++;
        }

        somaIdades += idade;

        alunos.push({
            nome,
            idade,
            situacao
        });
    }

    exibirAlunos();

    let media =
    calcularMedia(somaIdades, quantidade);

    exibirEstatisticas(
        quantidade,
        maiores,
        menores,
        media
    );
}

/*
Função para exibir alunos
*/
function exibirAlunos(){

    let html = `
        <table>
            <tr>
                <th>Nome</th>
                <th>Idade</th>
                <th>Situação</th>
            </tr>
    `;

    for(let aluno of alunos){

        let classe =
        aluno.idade >= 18 ? "maior" : "menor";

        html += `
            <tr>
                <td>${aluno.nome}</td>
                <td>${aluno.idade}</td>
                <td class="${classe}">
                    ${aluno.situacao}
                </td>
            </tr>
        `;
    }

    html += "</table>";

    document.getElementById("listaAlunos")
    .innerHTML = html;
}

/*
Função para exibir estatísticas
*/
function exibirEstatisticas(
    total,
    maiores,
    menores,
    media
){

    document.getElementById("estatisticas")
    .innerHTML = `
        <h2>Estatísticas da Turma</h2>

        <p>
            <strong>Total de alunos:</strong>
            ${total}
        </p>

        <p>
            <strong>Maiores de idade:</strong>
            ${maiores}
        </p>

        <p>
            <strong>Menores de idade:</strong>
            ${menores}
        </p>

        <p>
            <strong>Média de idade:</strong>
            ${media.toFixed(2)} anos
        </p>
    `;
}