# Aula 07 – "Projeto Integrado com Spring Boot e IA: de protótipo a sistema real" — roteiro do professor

Complementa o plano (`Aula07 – Projeto Integrado com Spring Boot e IA.docx`), os slides (21) e o
`Exercícios e Desafios - Aula07.docx`. Carga horária: **4 horas**.

## Arquivos da pasta

| Arquivo | Uso |
|---|---|
| `Aula07 – Projeto Integrado com Spring Boot e IA.docx` | Plano de aula com cronograma de 4h |
| `Aula07 – Projeto Integrado com Spring Boot e IA.pptx` | Slides: caça ao bug, camadas, JPA, fluxo, resiliência, cache, sprint, desafio |
| `Exercícios e Desafios - Aula07.docx` | Enunciados + rubrica |
| `aula07-atendimento-inteligente/` | **Gabarito** – sistema completo (MySQL por padrão, H2 por profile `h2`) |
| `aula07-kit-inicial/` | **Atenção:** hoje é uma cópia completa do gabarito (sem `TODO`) — peça uma versão com lacunas antes de distribuir aos alunos como exercício |

## Antes da aula (checklist – 20 min)

- [ ] Criar o banco no MySQL Workbench: `CREATE DATABASE atendimento CHARACTER SET utf8mb4;`. Confira o
      usuário/senha em `application.properties` (`root`/`admin` por padrão — ajuste se o MySQL da turma
      usar outra senha). Sem MySQL na máquina, rode com `--spring.profiles.active=h2` (sem instalar nada).
- [ ] Rodar o gabarito com `GEMINI_API_KEY`; cadastrar um usuário, perguntar 2× a mesma coisa com
      variações ("Vocês trocam tela?" / "voces TROCAM tela!!") e confirmar `CACHE HIT` no log. Conferir
      as tabelas `usuario`/`pergunta` pelo Workbench.
- [ ] Preparar a "quebra" do live coding: uma segunda Run Configuration com `ia.gemini.modelo=modelo-inexistente`
      (VM arguments: `-Dia.gemini.modelo=modelo-inexistente`) para mostrar o fallback sem mexer no código.
- [ ] **Se for usar `aula07-kit-inicial` como exercício da turma**, peça uma versão com os `TODO` de volta
      (hoje ele está completo, igual ao gabarito) antes de copiar para a pasta da turma.

## Aquecimento: "Onde mora o bug?" (15 min) — gabarito do slide 3

O Controller propositalmente errado tem, no mínimo, estes problemas:
1. Chave de API **hardcoded** no código (vaza no Git).
2. Chamada à IA **dentro do Controller** (sem Service; impossível reaproveitar/testar).
3. JSON montado por **concatenação de String** — quebra com aspas no texto e permite injeção no prompt.
4. **SQL por concatenação** — SQL injection clássico.
5. `throws Exception` — qualquer falha vira **500 com stack trace**; sem timeout, retry ou fallback.
6. `max_tokens: 9999` — sem controle de custo.
7. Devolve a resposta **bruta** do provedor (vaza estrutura interna) em vez de um DTO.
8. Sem validação da entrada (`Map<String,String>` em vez de DTO com `@Valid`).
9. Nada é persistido com metadados (origem, tokens, tempo) — sem auditoria.

Dupla que achar 7+ ganha o "Caçador de bugs" do dia.

## Gabarito do quiz relâmpago (slide 11)

1. **Service** — regra de negócio, reaproveitável, testável; o Controller só traduz HTTP.
2. `SELECT ... FROM pergunta WHERE usuario_id = ? ORDER BY data_hora DESC` (JPQL gerado pelo Spring Data).
3. **Não é 429 para o cliente.** Ou fallback com 201/200 e `origem = FALLBACK`, ou 502/503 com ProblemDetail — decisão de negócio, nunca stack trace.
4. Para que variações de maiúsculas, acentos e pontuação **batam** no cache ("Vocês trocam tela?" = "voces trocam tela").
5. **Variável de ambiente** (ou cofre de segredos). Properties só para configuração não sensível.

## Roteiro do live coding (45 min)

| Min | O que fazer | Fala-chave |
|---|---|---|
| 0-8 | `Usuario`, `Pergunta` (enum `Origem`), MySQL Workbench mostrando as tabelas criadas | "Entidade = tabela. `ddl-auto=update` criou tudo" |
| 8-15 | `PerguntaRepository`: consultas pelo nome + `@Query` | "Zero SQL para o CRUD" |
| 15-27 | `AtendimentoService.perguntar()` linha a linha | "cache → contexto → IA → fallback → save" |
| 27-33 | Controllers finos + `GlobalExceptionHandler` (404/409/400) | "ProblemDetail, nunca stack trace" |
| 33-40 | Swagger e página web: cadastrar, perguntar, repetir (HIT), histórico, métricas | "5 ms no cache × 1.500 ms na IA" |
| 40-45 | Reiniciar com modelo inválido → FALLBACK; `GET /api/metricas` | "A IA caiu, o sistema não" |

## Durante o sprint – erros comuns

| Sintoma | Causa | Solução |
|---|---|---|
| `LazyInitializationException` ao montar o DTO | acesso a `pergunta.getUsuario()` fora da transação | O gabarito usa `@Transactional` no Service e monta o DTO lá dentro |
| Histórico vazio depois de perguntar | esqueceram `perguntas.save(pergunta)` | Ex. 2 |
| Cache nunca dá HIT | `normalizar()` ainda é o `toLowerCase()` do kit / origem gravada errada | Só respostas com `origem = IA` entram no cache |
| Claude devolve 400 com histórico | histórico começando com mensagem do assistente | `montarHistorico()` do gabarito ignora fallbacks e alterna user/assistant |
| MySQL `Access denied` | usuário/senha diferente do configurado | Ajustar `spring.datasource.username/password` em `application.properties` (padrão `root`/`admin`) |
| `Communications link failure` / `Connection refused` | serviço MySQL parado | `services.msc` > iniciar "MySQL80", ou rodar com `--spring.profiles.active=h2` |
| Porta 8080 ocupada | app anterior aberto | `--server.port=8081` |
| 400 "JSON invalido" no curl do Windows | acentos em cp1252 | Testar pelo Swagger/página ou usar `--data-binary @arquivo.json` em UTF-8 |

## Pitch de 90 segundos (15 min)

Ordem sugerida: grupos com maior taxa de hit primeiro. Perguntas para provocar:
- Cache por texto ignora QUEM perguntou. Quando isso é problema? (respostas personalizadas, histórico).
- Validade de 60 min é boa para "horário de funcionamento"? E para "tem estoque?".
- Em produção, onde o cache ficaria? (Redis/Memcached; por quê não só o banco?).

## Ponte para a Aula 08 (avaliação)

Encerre mostrando a pasta `Aula08`: *"A prova prática é este projeto, enxuto, com 6 incrementos que
cobrem tudo do módulo. Quem entregou os exercícios de hoje já sabe fazer metade."*

## Pendências para o James

- MySQL agora é o padrão dos dois projetos (usuário/senha `root`/`admin`), com H2 disponível via
  `--spring.profiles.active=h2` para quem não tiver MySQL na máquina (ex.: notebook pessoal de aluno).
- `aula07-kit-inicial` está hoje **completo** (igual ao gabarito) — peça uma versão com `TODO` antes de
  usá-lo como exercício em sala.
- Conferir nomes de modelo no `application.properties`.
- Se a turma for grande, considerar `ia.max-tokens=300`.
