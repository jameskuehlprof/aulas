# Aula 06 – "Visão Computacional: a IA que enxerga as imagens da sua API" — roteiro do professor

Complementa o plano (`Aula06 – Visão Computacional.docx`), os slides (`.pptx`, 23 slides) e o
`Exercícios e Desafios - Aula06.docx`. Carga horária: **4 horas**.

## Arquivos da pasta

| Arquivo | Uso |
|---|---|
| `Aula06 – Visão Computacional.docx` | Plano de aula com cronograma de 4h |
| `Aula06 – Visão Computacional.pptx` | Slides: jogo, conceitos, formatos de imagem no JSON, ética/LGPD, live coding, sprint, desafio |
| `Exercícios e Desafios - Aula06.docx` | Enunciados + rubrica (entregar aos alunos) |
| `aula06-visao-web/` | **Gabarito** – projeto Spring Boot completo (compila e roda) |
| `aula06-kit-inicial/` | **Kit dos alunos** – `VisaoService` e `ReciboService` vazios com `TODO` |
| `imagens-teste/` | recibo nítido, recibo de baixa qualidade, cartão de visita (+ README) |

## Antes da aula (checklist – 20 min)

- [ ] Rodar `aula06-visao-web` com `GEMINI_API_KEY` e testar os 4 botões da página com
      `imagens-teste/recibo-nitido.png`. Confirmar que o recibo volta com `legivel: true`, total `100.00`
      e o aviso de "soma dos itens difere do total" (é proposital: há desconto de 2,95).
- [ ] Se tiver `ANTHROPIC_API_KEY`/`OPENAI_API_KEY`, testar também — a comparação entre provedores é
      o ponto alto do Exercício 1.
- [ ] Google Cloud Vision é **opcional**: exige projeto GCP com faturamento. Se não tiver, mostre só o
      JSON do slide 8 e o código de `GoogleCloudVision.java`; o endpoint responde 502 com mensagem clara.
- [ ] A foto do jogo "Humano × Máquina" já está pronta: `imagens-teste/cena-mesa-cafe.jpg` (mesa de café
      da manhã com ovos, waffles, frutas, café e suco — muitos elementos). Rodar `/api/visao/rotulos`
      nela antes da aula para ter o JSON de resposta já pronto para mostrar. Pode trocar por outra foto
      sua se preferir (mesmo critério: cena cheia de elementos).
- [ ] Avisar a turma no dia anterior: **tragam o celular com 3 fotos de objetos** (nunca rostos/documentos).

## Jogo de abertura: "Humano × Máquina" (15 min)

1. Projete a foto por **3 segundos**. Cada aluno anota 5 coisas que viu.
2. Mostre o JSON de rótulos da API (Gemini ou Cloud Vision) no telão.
3. Pergunte: quem acertou 3 ou mais? Que rótulos a máquina deu que ninguém anotou ("grama", "céu",
   "mesa")? Que rótulos as pessoas deram que a máquina não deu (contexto, intenção, emoção)?
4. Gancho: *"A máquina vê tudo, mas não sabe o que importa. Quem decide o que importa é o nosso código."*

## Gabarito do quiz relâmpago (slide 11)

1. **google-vision** (Cloud Vision) — API especializada, sem prompt.
2. **Base64** dentro do JSON (inline_data / image block / data URL / image.content).
3. Rosto é **dado biométrico = dado pessoal sensível (LGPD)**; exige base legal específica.
4. **Remover as cercas** ```` ```json ```` (`limparJson()`) e só então `mapper.readTree()`.
5. **Validar**: valor suspeito → aviso e não gravar; nunca confiar cegamente na extração.

## Roteiro do live coding (45 min)

| Min | O que fazer | Fala-chave |
|---|---|---|
| 0-5 | `VisaoProvider` + `GoogleCloudVision.perguntar()` lançando 501 | "Dois mundos: catálogo de features × prompt livre" |
| 5-12 | `Imagem.de()` e `base64()`; mostrar o tamanho do JSON de uma foto de 2 MB | "A foto vira texto. Validar antes de gastar tokens" |
| 12-22 | Os três providers multimodais lado a lado | "Mesma ideia, três lugares: inline_data, image block, image_url" |
| 22-30 | `MultimodalBase`: rótulos e OCR são prompts; `limparJson()` | "O modelo não sabe que você quer JSON. Você pede e confere" |
| 30-40 | Página web: rótulos e OCR com o recibo; trocar provedor | Comparar tempo e qualidade do OCR |
| 40-45 | Quebrar: `pom.xml` (415), sem arquivo (400), google-vision no /produto (400) | ProblemDetail, nunca stack trace |

## Durante o sprint – erros comuns

| Sintoma | Causa | Solução |
|---|---|---|
| 415 ao enviar foto do celular | `.HEIC` (iPhone) | Converter para JPEG (ajustes da câmera: "Mais compatível") ou enviar pelo WhatsApp Web (converte) |
| 413 | foto > 8 MB | Reduzir no Paint / enviar pelo WhatsApp |
| 502 "nao devolveu JSON valido" | modelo respondeu com texto antes do JSON | Reforçar "Responda SOMENTE com JSON"; ver `limparJson()` |
| OCR devolve `[SEM TEXTO]` na imagem de 200 px | resolução baixa demais | É o esperado no Ex. 2 — discutir a recomendação de captura |
| Recibo com `legivel: false` no nítido | prompt sem o campo `legivel` ou parse quebrando | Comparar com o `PROMPT` do gabarito |
| Gemini 429 | free tier estourado (muitas imagens) | Trocar para Claude/OpenAI ou usar chave da turma |

## Debate "Tribunal da IA" (últimos 15 min)

Perguntas para conduzir o debate sobre reconhecimento facial na portaria da escola:
- Qual é a finalidade? Existe alternativa menos invasiva (crachá, QR code)?
- Quem consente? Menores de idade?
- O que acontece com quem o sistema não reconhece (viés)? Quem responde pelo erro?
- Onde ficam as imagens, por quanto tempo, quem acessa?
Feche com: *"Tecnicamente é fácil (uma feature no JSON). Legal e eticamente, não."*

## Ponte para a Aula 07

*"Hoje a IA leu um recibo. Na próxima aula esse recibo, a pergunta do cliente e a resposta da IA vão
para um banco de dados, dentro de um sistema Spring Boot completo com JPA, MySQL e Swagger."*

## Pendências para o James

- Decidir se vale criar a chave do Google Cloud Vision (mostra bem a diferença "API especializada", mas
  exige cartão no GCP). Sem ela a aula funciona 100% com o Gemini.
- Confirmar nomes de modelo no `application.properties` (`gemini-3.8-flash`, `claude-haiku-4-5`, `gpt-4o-mini`).
- Reforçar no início da aula a regra: **nada de rostos nem documentos reais**.
