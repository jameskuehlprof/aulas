let corpo = document.body.children[3].textContent;

let subtitulo = document.getElementById('subtitulo');
let resultado = document.getElementById("resultado");

const sel1 = document.querySelector('#titulo');
const sel2 = document.querySelector('.destaque');
const sel3 = document.querySelector('p');
const sel4 = document.querySelector('.card');
const sel5 = document.querySelector('input[type="text"]');
const paragrafos = document.querySelectorAll('br');

paragrafos.forEach(function(paragrafo) {
  console.log(paragrafo.textContent);
});

// Ou com for...of:
for (const p of paragrafos) {
  p.style.color = 'blue';
}
// Acessar pelo índice:
console.log(paragrafos[0]); // Primeiro parágrafo
console.log(paragrafos.length); // Total encontrado

resultado.innerHTML = 
`<h3>Buscando Body: ${corpo}</h3> <br>
 <h3>Buscando Subtítulo: ${subtitulo.tagName}</h3><br>
  <h3>Buscando sel1: ${sel1.textContent}</h3><br>
 <h3>Buscando sel2: ${sel2.textContent}</h3><br>
 <h3>Buscando sel3: ${sel3.textContent}</h3><br>
 <h3>Buscando sel4: ${sel4.textContent}</h3><br>
 <h3>Buscando sel5: ${sel5.value}</h3><br>
 <h3>Buscando paragrafo: ${paragrafos.length}</h3><br> 
`;

resultado.innerHTML += '<span>Item extra</span>';
resultado.innerHTML += '<br><span>Item extra 2</span>';