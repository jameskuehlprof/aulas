# Aula 08 – Avaliação Final do Módulo — roteiro do professor

A Aula 08 foi **totalmente reformulada**: deixou de ser "projeto livre + tendências" e virou uma
avaliação incremental de 4 horas que cobre as Aulas 01 a 07. Os nomes dos arquivos originais foram
mantidos para não quebrar referências.

## Arquivos da pasta

| Arquivo | Uso | Quem vê |
|---|---|---|
| `Aula08 – Projeto Final e Tendências.docx` | Plano da avaliação: estrutura, cronograma, regras, incrementos | professor (pode publicar) |
| `Aula08 – Projeto Final e Tendências.pptx` | 13 slides de orientação (regras, nota, incrementos, defesa, encerramento) | turma |
| `Prova Objetiva - Aula08.docx` | Parte 1: 20 questões + cartão-resposta (3 páginas, imprimir) | alunos |
| `Exercícios e Desafios - Aula08.docx` | Parte 2 e 3: roteiro da prova prática (6 incrementos), rubrica resumida, defesa, autoavaliação | alunos |
| `Gabarito e Rubrica - Aula08 (professor).docx` | Gabarito da objetiva, critérios detalhados, descontos, banco de perguntas da defesa, composição da nota | **só professor** |
| `aula08-avaliacao-kit/` | Projeto-base da prova prática (compila; endpoints de ticket a criar) | alunos |
| `aula08-avaliacao-gabarito/` | Implementação completa + roteiro de teste com curl | **só professor** |

## Antes da aula (checklist – 30 min)

- [ ] Imprimir a **Prova Objetiva** (1 por aluno). Gabarito: `1-A 2-B 3-C 4-D 5-C 6-D 7-B 8-D 9-A 10-B 11-C 12-D 13-C 14-D 15-B 16-D 17-A 18-B 19-C 20-D`
      (também na tabela do docx do professor, com justificativas).
- [ ] Rodar o **gabarito** com a chave do Gemini e executar o roteiro de teste do README. Se algum nome de
      modelo mudou, ajustar `application.properties` **nos dois projetos** (kit e gabarito).
- [ ] Copiar o **kit** (sem `target/`) para a pasta da turma. Conferir que compila (`mvn -q compile`).
- [ ] Decidir a política de chaves: cada dupla usa a própria chave Gemini (gratuita) ou uma chave da turma
      no quadro, revogada ao final.
- [ ] Definir as duplas (sorteio evita "dupla forte × dupla fraca"). Com número ímpar, um trio faz o
      Incremento 6 completo + bônus de MySQL.
- [ ] Ter à mão 2-3 fotos de aparelhos (tela trincada, conector) para o Incremento 4, além das imagens da Aula 06.

## Condução

| Horário | O que fazer | Atenção |
|---|---|---|
| 00:00 | Slides 2-4: regras, nota, entrega. Reforçar: IA pode ajudar, mas cada um explica tudo na defesa | Chave no código = −6 |
| 00:10 | Prova objetiva. Computadores desligados. Recolher às 00:40 | Corrigir durante a prática |
| 00:45 | Kit importado; Swagger no ar; slides 6-9 ficam projetados | Só ajudar em problemas de **ambiente** |
| 00:45-02:45 | Circular. Anotar em uma planilha, por dupla, o que já está funcionando (facilita a correção) | Não dar dica de lógica |
| 02:45 | Intervalo de 15 min (o app pode ficar rodando) | |
| 03:00-03:30 | Finalização, README, zip. Lembrar do `Ctrl+H` por "sk-", "AIza" | Entrega até 03:30 |
| 03:15/03:30 | Defesas: 3 min por dupla, cronometradas. Com mais de 8 duplas, começar às 03:15 | Perguntas do banco no docx do professor |
| 03:55 | Autoavaliação (3 linhas no README) + fechamento: agentes, RAG, multimodal, MCP | |

## Correção (sugestão de fluxo)

1. Objetiva: 1,5 ponto por questão (cartão-resposta × gabarito) → até 30.
2. Prática: abrir o zip, `mvn spring-boot:run`, rodar o roteiro de curl do README do gabarito. Marcar cada
   incremento como Completo / Com ressalvas / Parcial / Ausente (100 / 70 / 40 / 0 %). Aplicar descontos.
3. Defesa: 4 (demonstração) + 3 + 3 (perguntas).
4. Nota = objetiva + prática + defesa. Aprovação: 60, com mínimo de 30 na prática.

## Problemas comuns durante a prova

| Sintoma | Causa | Ação permitida |
|---|---|---|
| Projeto não importa / `pom.xml` com erro | Maven offline sem dependências | Rodar `mvn -q compile` online uma vez com a rede do laboratório |
| `Variavel de ambiente ... nao configurada` | Run Configuration | Mostrar o caminho Run > Run Configurations > Environment |
| 429 no Gemini | free tier estourado (muitas duplas com a mesma chave) | Trocar para a chave reserva da turma |
| Porta 8080 ocupada | app anterior | `--server.port=8081` |
| Tudo devolve FALLBACK | chave errada | Conferir a chave (é ambiente, não lógica) |

## Pendências para o James

- Conferir os nomes de modelo antes da prova (mudam com frequência) nos dois projetos.
- Decidir duplas × individual para a prática (o material está escrito para duplas; para individual, reduza para os Incrementos 1, 2, 3 e 5 e mantenha a proporção de pontos).
- Se a turma for grande (> 16), planejar as defesas em paralelo a partir das 03:15.
