function enviar(){
const funcionario = {
nome:document.getElementById("nome").value,
salario:Number(document.getElementById("salario").value)
};
console.log(funcionario);
// Em um projeto com Spring Boot:
//
// fetch("http://localhost:8080/funcionarios", {
	//   method: "POST",
//   headers: {
//     "Content-Type": "application/json"
//   },
//   body: JSON.stringify(funcionario)
// });
}