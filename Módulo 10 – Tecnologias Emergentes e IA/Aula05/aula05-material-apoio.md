# Aula 05 – "APIs de IA Generativa: Claude, ChatGPT e Gemini em Java Web" — roteiro do professor

Material complementar ao plano de aula (`Aula05 – APIs de IA Generativa.docx`), aos slides
(`Aula05 – APIs de IA Generativa.pptx`) e ao documento `Exercícios e Desafios - Aula05.docx`.
Carga horária: **4 horas**.

## Arquivos da pasta

| Arquivo | Uso |
|---|---|
| `Aula05 – APIs de IA Generativa.docx` | Plano de aula (objetivos, cronograma de 4h, conteúdo, avaliação) |
| `Aula05 – APIs de IA Generativa.pptx` | 25 slides: roteiro, jogo, conceitos, live coding, sprint, desafio |
| `Exercícios e Desafios - Aula05.docx` | Enunciados completos + rubrica (entregar aos alunos) |
| `aula05-arena-ia/` | **Gabarito do professor** – projeto Spring Boot completo (compila e roda) |
| `aula05-kit-inicial/` | **Kit dos alunos** – mesmo projeto com os Services vazios (`TODO`) |

## Antes da aula (checklist – 20 min)

- [ ] Importar `aula05-arena-ia` no Eclipse e rodar com **pelo menos a chave do Gemini**
      (gratuita em aistudio.google.com/apikey). Ideal: as três chaves, para a Arena funcionar completa.
- [ ] Testar `POST /api/arena` no Swagger para confirmar que os nomes de modelo em
      `application.properties` ainda existem (`claude-haiku-4-5`, `gpt-4o-mini`, `gemini-3.8-flash`).
      Se algum retornar 404, troque o nome — é só uma propriedade.
- [ ] Copiar o `aula05-kit-inicial` para a pasta compartilhada da turma (ou zip).
- [ ] Preparar o jogo **"Quem respondeu?"** (abaixo).
- [ ] Decidir a política de chaves da turma: (a) cada aluno cria a própria chave Gemini
      (5 min, sem cartão); (b) o professor compartilha uma chave da turma pelo quadro e
      **revoga depois da aula**; (c) só o professor roda ao vivo e os alunos codificam contra o kit.

## Jogo de abertura: "Quem respondeu?" (15 min)

1. Antes da aula, envie a mesma pergunta pelo endpoint `/api/arena`:
   *"Explique para um cliente leigo, em 3 frases, por que o celular dele esquenta ao carregar."*
2. Cole as três respostas em um slide/quadro **sem identificar** o provedor (A, B, C).
3. A turma vota (mão levantada ou Mentimeter) em qual é Claude, ChatGPT e Gemini, e justifica
   ("essa parece mais formal", "essa usou emoji"...).
4. Revele com o JSON da Arena (mostra também `tempoMs` e tokens).
5. Gancho: *"Mesma pergunta, três respostas diferentes. Hoje vocês vão programar as três e
   descobrir o que muda por dentro."*

## Gabarito do quiz relâmpago (slide 12)

1. **Claude** (Anthropic) usa `x-api-key`; OpenAI usa `Authorization: Bearer`; Gemini usa `x-goog-api-key`.
2. No Gemini o papel do assistente é **`model`**.
3. Na OpenAI o system prompt é uma mensagem com `role: "system"` dentro de `messages`.
4. `finish_reason = "length"` (OpenAI) / `stop_reason = "max_tokens"` (Claude): a resposta **foi cortada** pelo limite de tokens.
5. **Falso** – a API não lembra nada; o nosso código reenvia o histórico a cada chamada.

## Roteiro do live coding (45 min)

| Min | O que fazer | Fala-chave |
|---|---|---|
| 0-5 | Abrir o kit, mostrar as pastas `config/ ia/ service/ controller/ static/` | "Mesma arquitetura em camadas que veremos na Aula 07" |
| 5-15 | Ler `ClaudeProvider`, `OpenAiProvider`, `GeminiProvider` lado a lado | "Três diferenças: header, papel, caminho do texto. O resto é igual" |
| 15-20 | `ProviderBase.postJson`: timeout, retry, `IaException` | "Erro de API externa nunca derruba a nossa API" |
| 20-25 | Implementar `ChatService.chat()` (1 linha) e testar no Swagger | "Controller fino, Service com a regra" |
| 25-35 | Página Arena: mesma pergunta, comparar tempo e tokens | "Tokens = dinheiro. O que não se mede, não se controla" |
| 35-45 | Quebrar de propósito: chave errada, provedor `xpto`, mensagem vazia | Mostrar `ProblemDetail` no lugar de stack trace |

## Durante o sprint (70 min) – erros comuns e como destravar

| Sintoma | Causa provável | Solução |
|---|---|---|
| `Variavel de ambiente ... nao configurada` | Run Configuration sem a variável | Run > Run Configurations > Environment > New |
| 502 "Chave de API invalida" | chave copiada com espaço/aspas | Recolar a chave; testar no Swagger de novo |
| 502 "Limite de uso ou credito esgotado" | conta sem crédito (OpenAI/Anthropic) ou free tier do Gemini estourado | Trocar o provedor no JSON; usar chave da turma |
| 502 "Modelo nao encontrado" | nome de modelo desatualizado | Ajustar `ia.*.modelo` no `application.properties` |
| Resposta cortada no meio | `ia.max-tokens` baixo | Aumentar para 1000 e discutir o custo |
| Gemini devolve 400 com histórico | primeira mensagem não é `user` ou papéis alternados errado | Ver `AtendimentoService.limitar()` no gabarito |
| Porta 8080 ocupada | outra instância rodando | `--server.port=8081` na Run Configuration (VM args) ou parar a outra |

## Fechamento (15 min)

- Arena final: 2 min por grupo. Peça sempre a pergunta **fora do escopo** primeiro — é onde as
  personalidades se diferenciam.
- Votação rápida; anote o grupo vencedor para o "Hall da Fama" do módulo.
- Ponte para a Aula 06: *"Hoje a IA leu texto. Na próxima ela vai enxergar as imagens que a nossa
  API receber — o mesmo `IaProvider`, agora com uma imagem em base64 dentro do JSON."*

## Custos (estimativa para uma turma de 25 alunos)

Cada exercício gera ~10 chamadas por dupla com ~300 tokens de entrada e ~200 de saída. Uma turma
inteira em 4 horas fica na casa de **algumas dezenas de milhares de tokens por provedor**: centavos
de dólar em modelos "mini/flash/haiku". O único risco real é alguém colocar um texto gigante no
Exercício 2 — por isso o DTO limita o texto a 12.000 caracteres.

## Pendências para o James

- Confirmar os nomes de modelo antes da aula (mudam com frequência).
- Decidir a política de chaves (item do checklist) e, se for compartilhar uma chave, **revogá-la ao final**.
- Se a turma for grande, considere reduzir `ia.max-tokens` para 400 no kit inicial.
