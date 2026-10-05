function calcularMedia(){

    let valor1 = Number(document.getElementById("valor1").value);
    let valor2 = Number(document.getElementById("valor2").value);
    let valor3 = Number(document.getElementById("valor3").value);
    let valor4 = Number(document.getElementById("valor4").value);

    let frequencia = Number(
        document.getElementById("frequencia").value
    );

    let media = (valor1 + valor2 + valor3 + valor4) / 4;

    let situacao;
    let cor;
    let corsituacao;
    let situacaofreq;

    if(frequencia < 75){
      situacaofreq = "REPROVADO POR FALTA";
      corsituacao = "vermelho";
    }
    else {
        situacaofreq = "Aprovado Frequencia";
      corsituacao = "verde";
    }
    
    if(media >= 7){
        situacao = "APROVADO";
        cor = "verde";
    }
    else if(media >= 5){
        situacao = "RECUPERAÇÃO";
        cor = "laranja";
    }
    else{
        situacao = "REPROVADO (RUIM)";
        cor = "vermelho";
    }

    let agora = new Date();

    let dataHora =
        agora.toLocaleDateString("pt-BR") +
        " " +
        agora.toLocaleTimeString("pt-BR");

    document.getElementById("resultado").innerHTML =
    `
    <p>Média: ${media.toFixed(2)}</p>
    
    <p>Frequência: ${frequencia}%</p>
    
    <p class="${cor}">
        Situação Média: ${situacao}
    </p>
   <p class="${corsituacao}">
        Situação Frequencia: ${situacaofreq}
    </p>
    <p class="datahora">
        Cálculo realizado em:
        ${dataHora}
    </p>
    `;
}