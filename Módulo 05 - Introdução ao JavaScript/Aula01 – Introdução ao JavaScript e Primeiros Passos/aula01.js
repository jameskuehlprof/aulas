// AULA 01 — Variáveis, Constantes, prompt, alert, console.log
// CONSTANTES (const) 
// Valores que NÃO mudam durante a execução do programa
const VERSAO_SISTEMA = "1.0";          // texto (string)
const ANO_ATUAL      = 2026;           // número inteiro
const PI             = 3.14159;        // número decimal
const ATIVO          = true;           // booleano (verdadeiro/falso)
// VARIÁVEIS (let)
// Valores que PODEM ser alterados durante a execução
let nome;       // declarada, ainda sem valor (undefined)
let idade;
let cidade;
let saudacao;
// ENTRADA DE DADOS — prompt()
// prompt() exibe uma caixa de diálogo e retorna o que o
// usuário digitou como texto (string)
nome   = prompt("Digite seu nome:");
idade  = prompt("Digite sua idade:");
cidade = prompt("De qual cidade você é?");
// PROCESSAMENTO
// Conversão: prompt sempre retorna string; para operar
// matematicamente, convertemos com Number()
let idadeNumero = Number(idade);       // "10" → 10
let anoNascimento = ANO_ATUAL - idadeNumero;
let qtmeses = idadeNumero * 12;
// Construindo a saudação com concatenação (+)
saudacao = "Olá, " + nome + "! Você tem " + idadeNumero
         + " anos e nasceu por volta de " + anoNascimento + ".";
// SAÍDA — alert()
// alert() exibe uma caixa de diálogo com uma mensagem
alert(saudacao);
alert("Passou-se " + qtmeses + " meses!" );
alert("Bem-vindo ao sistema " + VERSAO_SISTEMA + " 🚀");
// SAÍDA — console.log()
// console.log() imprime no console do navegador (F12)
// Muito usado para depuração (debug)
console.log("=== Dados do usuário ===");
console.log("Nome:          " + nome);
console.log("Idade:         " + idadeNumero + " anos");
console.log("Cidade:        " + cidade);
console.log("Ano nascimento:", anoNascimento);
console.log("Qtde meses: ", qtmeses);
console.log("Sistema ativo:", ATIVO);
console.log("PI usado:     ", PI);
console.log("========================");
// VARIÁVEL: pode ser reatribuída 
// Exemplo: atualizando a cidade
let cidadeAtual = cidade;
console.log("Cidade original:", cidadeAtual);
cidadeAtual = "Nova Cidade";           // reatribuição válida com let
console.log("Cidade atualizada:", cidadeAtual);
// TIPOS DE DADOS
console.log("--- Tipos de dados ---");
console.log(typeof nome);              // "string"
console.log(typeof idadeNumero);       // "number"
console.log(typeof ATIVO);            // "boolean"
console.log(typeof undefined);       // "undefined"
console.log("Tipo da var. nome é ", typeof nome);        