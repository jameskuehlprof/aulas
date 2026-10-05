# Aula 05 – Arena de IA Generativa (Claude × ChatGPT × Gemini)

Projeto Spring Boot de apoio à Aula 05 do Módulo 10 – Tecnologias Emergentes e IA.
É a **versão "Java for Web"** dos exemplos de console da Aula 04: os mesmos
`HttpClient` + Jackson, agora dentro de uma API REST com Swagger e uma página web.

```
src/main/java/br/com/senac/aula05
├── Aula05Application.java          # main
├── config/IaProperties.java        # lê ia.* do application.properties
├── ia/                             # "porta" para os provedores
│   ├── IaProvider.java             # interface comum (nome + conversar)
│   ├── ProviderBase.java           # HttpClient, timeout, retry, erros
│   ├── ClaudeProvider.java         # Anthropic  (x-api-key)
│   ├── OpenAiProvider.java         # OpenAI     (Authorization: Bearer)
│   ├── GeminiProvider.java         # Google     (x-goog-api-key)
│   ├── IaProviderFactory.java      # escolhe pelo nome
│   ├── MensagemChat / RespostaIA   # formato neutro
│   └── IaException.java
├── service/ChatService.java        # Exercícios 1, 2, 3 + Arena
├── service/AtendimentoService.java # Desafio (system prompt + histórico)
├── controller/…                    # endpoints REST + tratamento de erro
└── resources/static/index.html     # página "Arena" e chat da Lia
```

## Como rodar

Pré-requisitos: JDK 17+ e as chaves de API (as mesmas da Aula 04).

1. **Eclipse:** File > Import > Maven > Existing Maven Projects → pasta `aula05-arena-ia`.
2. Botão direito em `Aula05Application.java` > Run As > Java Application (vai falhar
   nas chamadas até configurar as chaves — é esperado).
3. **Run > Run Configurations > aba Environment > New**, uma linha para cada:
   - `ANTHROPIC_API_KEY` – console.anthropic.com
   - `OPENAI_API_KEY` – platform.openai.com
   - `GEMINI_API_KEY` – aistudio.google.com/apikey (tem nível gratuito)
4. Apply > Run. Acesse:
   - Página Arena: <http://localhost:8080/>
   - Swagger UI: <http://localhost:8080/swagger-ui.html>

Pelo terminal (PowerShell):

```powershell
$env:ANTHROPIC_API_KEY="..."; $env:OPENAI_API_KEY="..."; $env:GEMINI_API_KEY="..."
mvn spring-boot:run
```

> Só tem uma chave? Sem problema: cada endpoint recebe o campo `provedor`
> e os que não tiverem chave respondem com um erro amigável (não derrubam a API).

## Endpoints

| Método | Rota | Para quê | Exercício |
|---|---|---|---|
| POST | `/api/chat` | pergunta única para `claude`, `openai` ou `gemini` | Ex. 1 |
| POST | `/api/resumo` | resume um texto em N linhas | Ex. 2 |
| POST | `/api/email-cobranca` | gera e-mail a partir de nome, valor, prazo e tom | Ex. 3 |
| POST | `/api/arena` | mesma pergunta para os 3 em paralelo + quem foi mais rápido | Bônus |
| POST | `/api/atendimento/{sessaoId}` | assistente "Lia" com personalidade e memória | Desafio |
| GET | `/api/atendimento/{sessaoId}/historico` | mostra o que a Lia "lembra" | Desafio |
| DELETE | `/api/atendimento/{sessaoId}` | apaga a sessão | Desafio |

Exemplo (Swagger ou curl):

```bash
curl -X POST http://localhost:8080/api/chat -H "Content-Type: application/json" \
  -d '{"provedor":"gemini","mensagem":"Explique em 2 frases o que é uma API REST."}'
```

Resposta:

```json
{
  "provedor": "gemini",
  "modelo": "gemini-3.8-flash",
  "texto": "Uma API REST é ...",
  "tokensEntrada": 21,
  "tokensSaida": 58,
  "tempoMs": 940,
  "totalTokens": 79
}
```

## Configuração (`application.properties`)

| Propriedade | Padrão | Observação |
|---|---|---|
| `ia.claude.modelo` | `claude-haiku-4-5` | mais barato da Anthropic; `claude-opus-5` é o mais capaz |
| `ia.openai.modelo` | `gpt-4o-mini` | confira em platform.openai.com/docs/models |
| `ia.gemini.modelo` | `gemini-3.8-flash` | `gemini-3.5-flash-lite` é o mais barato |
| `ia.max-tokens` | `600` | limite de tokens de **saída** (custo × tamanho) |
| `ia.timeout-segundos` | `40` | por chamada HTTP |
| `ia.historico.tamanho` | `10` | mensagens lembradas pela Lia |

## Como este projeto vira o "kit inicial" dos alunos

Os alunos **não precisam digitar os três providers do zero** — o objetivo da aula
é integrar IA em uma API web, não reescrever HTTP. Sugestão de uso:

- **Exercícios 1 a 3:** entregar o projeto **sem** `ChatService` e `ChatController`
  (ou com os métodos vazios lançando `UnsupportedOperationException`) e pedir que
  implementem. As classes do pacote `ia/` ficam como estão.
- **Desafio:** entregar sem `AtendimentoService`/`AtendimentoController`; o grupo
  cria o system prompt da **própria empresa fictícia** e o histórico por sessão.
- O projeto completo é o **gabarito do professor**.
