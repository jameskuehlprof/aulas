// ═══════════════════════════════════════════════
//  DADOS — exatamente como na aula original
// ═══════════════════════════════════════════════

let frutas = ["Maçã", "Banana", "Laranja"];

let alunos = ["Maria", "Tião"];
alunos.push("Marcos");
// pop remove o último
alunos.pop();

let aluno = {
    nome: "João", idade: 18, curso: "JavaScript"
};
aluno.idade = 19;
aluno.cidade = "Blumenau";

let aluno_teste = {
    nome: "Maria",
    apresentar: function() {
        return "Olá, meu nome é " + this.nome;
    }
};

let alunos_obj = [
    { nome: "Ana",   idade: 17 },
    { nome: "Pedro", idade: 18 }
];

let alunos_arrays = ["José", "Marta", "Thiago"];

// ═══════════════════════════════════════════════
//  RENDERIZAÇÃO
// ═══════════════════════════════════════════════

// ── 1. Frutas ────────────────────────────────────
const frutasList = document.getElementById("frutas-list");
frutas.forEach((fruta, i) => {
    const item = document.createElement("div");
    item.className = "array-item";
    item.innerHTML = `
        <span class="array-index">[${i}]</span>
        <div class="array-value">${fruta}</div>
    `;
    frutasList.appendChild(item);
});

// ── 2. push · pop · length ───────────────────────
const alunosSteps = document.getElementById("alunos-steps");

const passos = [
    {
        op: "push", opClass: "push",
        desc: `push("Marcos")`,
        arr: ["Maria", "Tião", "Marcos"]
    },
    {
        op: "pop", opClass: "pop",
        desc: "pop()",
        arr: ["Maria", "Tião"]
    },
    {
        op: "length", opClass: "len",
        desc: ".length",
        arr: null,
        result: alunos.length
    }
];

passos.forEach(p => {
    const step = document.createElement("div");
    step.className = "step";

    let arrHtml = "";
    if (p.arr) {
        const items = p.arr.map(a => `<span>[${a}]</span>`).join(" ");
        arrHtml = `<span class="step-arr">→ ${items}</span>`;
    } else {
        arrHtml = `<span class="step-arr">→ <span>${p.result}</span></span>`;
    }

    step.innerHTML = `
        <span class="step-op ${p.opClass}">${p.desc}</span>
        ${arrHtml}
    `;
    alunosSteps.appendChild(step);
});

// ── 3. Objeto aluno ──────────────────────────────
const alunoObj = document.getElementById("aluno-obj");

const props = [
    { key: "nome",   val: aluno.nome,   tipo: "original" },
    { key: "idade",  val: aluno.idade,  tipo: "changed" },
    { key: "curso",  val: aluno.curso,  tipo: "original" },
    { key: "cidade", val: aluno.cidade, tipo: "new" }
];

props.forEach(p => {
    const div = document.createElement("div");
    div.className = "obj-prop" + (p.tipo !== "original" ? ` ${p.tipo}-prop` : "");

    let badge = "";
    if (p.tipo === "new")     badge = `<span class="prop-badge new">novo</span>`;
    if (p.tipo === "changed") badge = `<span class="prop-badge changed">alterado</span>`;

    div.innerHTML = `
        ${badge}
        <span class="prop-key">${p.key}</span>
        <span class="prop-val">${p.val}</span>
    `;
    alunoObj.appendChild(div);
});

// ── 4. Método apresentar ──────────────────────────
const metodoOutput = document.getElementById("metodo-output");
metodoOutput.textContent = aluno_teste.apresentar();

// ── 5. Array de objetos ───────────────────────────
const alunosObjList = document.getElementById("alunos-obj-list");

alunos_obj.forEach((a, i) => {
    const inicial = a.nome.charAt(0);
    const card = document.createElement("div");
    card.className = "student-card";
    card.innerHTML = `
        <div class="student-avatar">${inicial}</div>
        <div class="student-info">
            <span class="student-name">${a.nome}</span>
            <span class="student-meta">idade: ${a.idade}</span>
        </div>
        <span class="student-index">[${i}]</span>
    `;
    alunosObjList.appendChild(card);
});

// ── 6. Iteração for / forEach ─────────────────────
const forList     = document.getElementById("for-list");
const foreachList = document.getElementById("foreach-list");

// for clássico
for (let i = 0; i < alunos_arrays.length; i++) {
    const item = document.createElement("div");
    item.className = "iter-item";
    item.innerHTML = `<span class="iter-i">i=${i}</span> ${alunos_arrays[i]}`;
    forList.appendChild(item);
}

// forEach
alunos_arrays.forEach(function(nome, i) {
    const item = document.createElement("div");
    item.className = "iter-item";
    item.innerHTML = `<span class="iter-i">→</span> ${nome}`;
    foreachList.appendChild(item);
});
