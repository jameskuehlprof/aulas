function calcularMedia(){
    // Obtém valores digitados
    let valor1 = Number(document.getElementById("valor1").value);
    let valor2 = Number(document.getElementById("valor2").value);
    // Calcula média
    let media = (valor1 + valor2) / 2;
    let situacao;
    // Verifica situação
    if(media >= 7){
        situacao = "APROVADO";
    }
    else if(media >= 5){
        situacao = "RECUPERAÇÃO";
    }
    else{
        situacao = "REPROVADO";
    }
    // Exibe resultado
    document.getElementById("resultado")
        .innerHTML =
        `
        Média: ${media.toFixed(1)}
        <br>
        Situação: ${situacao}
        `;
}