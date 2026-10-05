 function somarvalores(param1, param2) {
    let resultado = param1 + param2;
    //console.log(resultado);
    return resultado;
}
 /*let resultado = multiplicar(5, 4);
 console.log(resultado);
*/
 //  let senha = Number(document.getElementById("senha").value);
let senha = "";
while (senha !== "1234") {
    senha = prompt("Digite a senha:");
}
alert("Acesso permitido!");
//let senha = "";
/*do {
    senha = prompt("Digite a senha:");
}
while (senha !== "1234")
alert("Acesso permitido!");*/

