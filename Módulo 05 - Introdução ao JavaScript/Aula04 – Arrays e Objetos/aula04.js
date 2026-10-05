let frutas = ["Maçã", "Banana", "Laranja"];
console.log(frutas);
console.log("-------------------");
console.log(frutas[2]);

let alunos = ["Maria", "Tião"];
alunos.push("Marcos");
console.log(alunos);

alunos.pop();
console.log(alunos);
console.log(alunos.length);

//Objetos
let aluno = {
    nome: "João",  idade: 18,  curso: "JavaScript"
};
console.log(aluno);

//Acessando propriedades
console.log(aluno.nome);
console.log(aluno.idade);

//Alterando propriedade
aluno.idade = 19;
console.log(aluno);

//Adicionando propriedades
//let aluno1 = {  nome: "João"};
aluno.cidade = "Blumenau";
console.log(aluno);

//Métodos em objetos
let aluno_teste = {
    nome: "Maria",
    listar: function() {
        console.log("Olá, meu nome é " + this.nome);
    }
};
aluno_teste.listar();

//Arrays de objetos
let alunos_obj = [
    {
        nome: "Ana",
        idade: 17
    },
    {
        nome: "Pedro",
        idade: 18
    },
    {
        nome: "Gabriel",
        idade: 9
    }
];

console.log(alunos_obj);
//Acessando dados
console.log(alunos_obj[0].nome);
console.log(alunos_obj[1].idade);

//Percorrendo  arrays
let alunos_arrays = ["José", "Marta", "Thiago"];

for(let i = 0; i < alunos_arrays.length; i++) {
    console.log(alunos_arrays[i]);
}
console.log("----------------");
alunos_arrays.forEach(function(aluno){
    console.log(aluno);
});
