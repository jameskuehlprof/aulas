# Aula 04 — Passo a passo: Claude API e Gemini API no Eclipse

Este documento complementa o `aula04-material-apoio.md` e o `GeradorTextoIA.java`
(exemplo com OpenAI já existente). Ele não substitui nada — mostra como rodar
os dois novos exemplos (`ClaudeChat.java` e `GeminiChat.java`) que consomem a
API da Anthropic (Claude) e a API do Google (Gemini), dentro do mesmo projeto
Maven `projeto-maven-aula04`.

**Por que ensinar as três?** Pedagogicamente é ótimo: são três APIs de IA
bem conhecidas, mas cada uma manda a chave de API de um jeito diferente
(Bearer, `x-api-key`, query param `?key=`) e usa um formato de JSON
ligeiramente diferente. Isso reforça a ideia central da Aula 04 (slide 4:
"mesma estrutura, dois cenários") — o aluno vê que aprender *uma* API REST
bem ensina a ler qualquer outra.

> ⚠️ Nomes de modelo e preços mudam com frequência. Os nomes usados aqui
> (`claude-haiku-4-5` e `gemini-3.8-flash`) estavam corretos em setembro de
> 2026 — confira a lista atual em platform.claude.com e ai.google.dev antes
> da aula.

---

## 1. Conseguir a chave da Claude API (Anthropic)

1. Acesse **console.anthropic.com** e crie uma conta (ou faça login).
2. No menu lateral, vá em **Settings > API Keys**.
3. Clique em **Create Key**, dê um nome (ex.: `senac-aula04`) e copie o valor
   gerado — ele só aparece uma vez.
4. Assim como já foi descoberto com a OpenAI (ver `aula04-material-apoio.md`),
   a Anthropic **não** dá crédito gratuito automático para contas novas.
   É preciso ir em **Settings > Billing** e cadastrar um cartão + comprar um
   crédito pequeno (uns US$5 já é suficiente para uma turma inteira usando
   `claude-haiku-4-5`, que é o modelo mais barato).
5. **Decisão a tomar antes da aula:** mesma escolha já documentada para a
   OpenAI — (a) colocar crédito na sua própria conta, (b) verificar se o
   SENAC tem chave institucional, ou (c) usar `ClaudeChat.java` só como
   gabarito explicado, sem rodar ao vivo com chave real.

## 2. Conseguir a chave da Gemini API (Google)

1. Acesse **aistudio.google.com/apikey** e faça login com uma conta Google.
2. Clique em **Create API key** (ou **Get API key**) e escolha um projeto
   Google Cloud (ele cria um automaticamente se você não tiver nenhum).
3. Copie a chave gerada.
4. **Boa notícia para a aula:** ao contrário da OpenAI e da Anthropic, o
   Google costuma oferecer um nível gratuito de uso na AI Studio que não
   exige cartão de crédito cadastrado, com um limite diário de requisições
   suficiente para uma demonstração em sala. **Confirme isso em
   aistudio.google.com/apikey antes da aula** — políticas de free tier
   mudam com frequência e isso evita surpresa no dia.
5. Se quiser o modelo mais econômico possível, troque `MODEL` em
   `GeminiChat.java` de `gemini-3.8-flash` para `gemini-3.5-flash-lite`.

## 3. Importar o projeto no Eclipse (se ainda não fez)

Mesmo passo já documentado para o projeto original:

1. **File > Import > Maven > Existing Maven Projects**.
2. Aponte para a pasta `Aula04/projeto-maven-aula04`.
3. Finish. O Eclipse baixa as dependências do Maven (Jackson) automaticamente.

Se o projeto já estava importado de antes, os dois arquivos novos
(`ClaudeChat.java` e `GeminiChat.java`) aparecem sozinhos dentro do pacote
`br.com.senac.aula04` — não precisa reimportar nada.

## 4. Configurar as variáveis de ambiente no Eclipse

Cada classe lê sua chave com `System.getenv(...)`, então cada uma precisa da
sua própria Run Configuration com a variável certa — **elas não se
misturam automaticamente** (mesma observação já registrada no material de
apoio: uma variável configurada numa Run Configuration não aparece em outra,
nem no PowerShell).

### Para `ClaudeChat.java`

1. Clique com o botão direito em `ClaudeChat.java` > **Run As > Java
   Application** (isso cria a Run Configuration; vai dar erro na primeira
   vez, é esperado — a chave ainda não foi configurada).
2. **Run > Run Configurations...**, selecione `ClaudeChat` na lista.
3. Aba **Environment > New**:
   - Name: `ANTHROPIC_API_KEY`
   - Value: (cole a chave copiada no passo 1)
4. **Apply > Run**.

### Para `GeminiChat.java`

Mesma sequência, só troca o nome da classe e da variável:

1. Botão direito em `GeminiChat.java` > **Run As > Java Application**.
2. **Run > Run Configurations...** > `GeminiChat` > aba **Environment > New**:
   - Name: `GEMINI_API_KEY`
   - Value: (cole a chave copiada no passo 2)
3. **Apply > Run**.

## 5. Rodar sem configurar nada no Eclipse (alternativa via terminal)

Se preferir demonstrar pelo terminal em vez da Run Configuration (mesma
lógica do `mvn exec:java` já documentado no `pom.xml`):

```bash
# Linux/macOS
export ANTHROPIC_API_KEY="sua-chave-aqui"
mvn exec:java -Dexec.mainClass="br.com.senac.aula04.ClaudeChat"

export GEMINI_API_KEY="sua-chave-aqui"
mvn exec:java -Dexec.mainClass="br.com.senac.aula04.GeminiChat"
```

```powershell
# Windows PowerShell — repare no "$env:", igual já explicado no material de
# apoio para a chave da OpenAI
$env:ANTHROPIC_API_KEY="sua-chave-aqui"
mvn exec:java "-Dexec.mainClass=br.com.senac.aula04.ClaudeChat"

$env:GEMINI_API_KEY="sua-chave-aqui"
mvn exec:java "-Dexec.mainClass=br.com.senac.aula04.GeminiChat"
```

## 6. O que esperar rodando

Se tudo estiver certo, a saída no console é parecida com:

```
Pergunta: Explique em até 3 frases o que é uma API REST...
Resposta da Claude: Uma API REST é uma forma padronizada de dois sistemas
conversarem pela internet usando HTTP...
```

## 7. As três APIs lado a lado

| | OpenAI (`GeradorTextoIA.java`) | Claude (`ClaudeChat.java`) | Gemini (`GeminiChat.java`) |
|---|---|---|---|
| Chave vai em | Header `Authorization: Bearer <chave>` | Header `x-api-key: <chave>` | Query param na URL `?key=<chave>` |
| Header extra obrigatório | — | `anthropic-version: 2023-06-01` | — |
| Nome do modelo vai em | corpo JSON (`model`) | corpo JSON (`model`) | **na própria URL** (`/models/{modelo}:generateContent`) |
| Onde fica o texto da resposta | `choices[0].message.content` | `content[0].text` | `candidates[0].content.parts[0].text` |
| Precisa cartão para começar? | Sim | Sim | Normalmente não (confirmar antes da aula) |

Esse quadro é um ótimo slide/material de apoio extra — mostra visualmente
que "consumir API de IA" não é uma coisa só, são pequenas variações do
mesmo padrão REST que a Aula 04 já ensina.

## 8. Solução de problemas específicos do Claude e do Gemini

Além dos erros já documentados para a OpenAI (`aula04-material-apoio.md`,
seção "Solução de problemas"):

- **Claude — 401 "authentication_error"**: geralmente é a chave copiada com
  espaço em branco extra no fim (comum ao copiar do navegador). Confira o
  valor exato na Run Configuration.
- **Claude — 429 com `type: "rate_limit_error"` vs `type:
  "insufficient_quota"`**: mesma lógica já documentada para a OpenAI — o
  campo `type` dentro do corpo JSON do erro diz qual é o problema real.
- **Gemini — 403 `PERMISSION_DENIED`**: geralmente é a API key certa, mas
  criada num projeto Google Cloud onde a "Generative Language API" não foi
  habilitada. Resolve em [console.cloud.google.com/apis/library](https://console.cloud.google.com/apis/library),
  procurando "Generative Language API" e clicando em **Enable**.
- **Gemini — resposta vazia com status 200**: como comentado direto no
  código de `GeminiChat.java`, o Gemini pode bloquear o conteúdo por
  segurança (`finishReason: "SAFETY"`) sem gerar nenhum texto, mesmo
  respondendo HTTP 200 — mesmo tipo de "pegadinha" já visto com a ViaCEP na
  `ConsultaCepHttp.java` (200 não significa sempre sucesso).

## Pendências para o James

- Decidir se vai rodar `ClaudeChat.java` e `GeminiChat.java` ao vivo em sala
  ou só usá-los como gabarito explicado (mesma decisão já pendente para o
  `GeradorTextoIA.java`).
- Confirmar perto da data da aula se o free tier do Gemini continua sem
  exigir cartão (ver seção 2).
- Se decidir rodar Gemini ao vivo, testar uma vez no Eclipse antes — os
  outros dois exemplos (OpenAI e Claude) já foram testados de verdade pelo
  James; o Gemini ainda não.
