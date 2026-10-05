# Aula 07 – Sistema de Atendimento Inteligente (Spring Boot + JPA + IA)

Projeto integrado da Aula 07: a integração de IA das Aulas 04-06 entra em uma aplicação Spring Boot
**real**, organizada em camadas, com persistência em banco de dados, cache, fallback e Swagger.

```
src/main/java/br/com/senac/aula07
├── Aula07Application.java
├── config/IaProperties.java            # ia.* (provedor, modelos, cache)
├── ia/                                  # mesmos providers da Aula 05 (Claude, OpenAI, Gemini)
├── model/Usuario.java, Pergunta.java    # entidades JPA (tabelas usuario e pergunta)
├── repository/…Repository.java          # Spring Data JPA (consultas pelo nome do método)
├── service/UsuarioService.java
├── service/CacheService.java            # Desafio: cache por texto normalizado, no banco
├── service/AtendimentoService.java      # AQUI a IA é chamada: cache → contexto → IA → fallback → persistência
├── controller/…Controller.java          # controllers finos + GlobalExceptionHandler (ProblemDetail)
└── resources/static/index.html          # chat web com usuários, histórico e métricas
```

## Como rodar

**Com MySQL (padrão – requer MySQL Workbench/Server instalado):**

```sql
CREATE DATABASE atendimento CHARACTER SET utf8mb4;
```

1. Import no Eclipse (Existing Maven Project) → `aula07-atendimento-inteligente`.
2. Confira usuário/senha do MySQL em `application.properties` (`root`/`admin` por padrão — ajuste se o
   seu MySQL usar outra senha).
3. Run Configurations > Environment: `GEMINI_API_KEY` (ou a chave do provedor escolhido em `ia.provedor`).
4. Run `Aula07Application`. Página: <http://localhost:8080/> · Swagger: <http://localhost:8080/swagger-ui.html>.
   As tabelas (`usuario`, `pergunta`) são criadas automaticamente (`ddl-auto=update`) — confira pelo
   MySQL Workbench.

**Sem MySQL instalado (H2 em memória, opcional):**

Run Configurations > Arguments > Program arguments: `--spring.profiles.active=h2`
(nada para instalar). Console H2: <http://localhost:8080/h2-console> (JDBC URL `jdbc:h2:mem:atendimento`,
usuário `sa`, sem senha).

> Sem chave de IA o sistema **continua no ar**: toda pergunta recebe a resposta de fallback e fica
> registrada com `origem = FALLBACK`. É de propósito — erro de API externa não derruba o sistema.

## Endpoints

| Método | Rota | Descrição | Exercício |
|---|---|---|---|
| POST | `/api/usuarios` | cadastra usuário (409 se e-mail repetido) | base |
| GET | `/api/usuarios` | lista usuários | base |
| POST | `/api/perguntas` | `{usuarioId, texto}` → resposta da IA / cache / fallback, persistida | Ex. 1, 3, Desafio |
| GET | `/api/usuarios/{id}/historico` | perguntas e respostas do usuário, mais recente primeiro | Ex. 2 |
| GET | `/api/metricas` | total, IA × cache × fallback, taxa de hit, tempo médio, tokens | Desafio |

Resposta de `POST /api/perguntas`:

```json
{
  "id": 7, "usuarioId": 1,
  "texto": "Vocês trocam tela de iPhone?",
  "resposta": "Sim! Trocamos telas de iPhone...",
  "origem": "IA",            // IA | CACHE | FALLBACK
  "provedor": "gemini", "modelo": "gemini-3.8-flash",
  "tokensEntrada": 212, "tokensSaida": 61, "tempoMs": 1340,
  "categoria": "orcamento",  // bônus: classificação automática
  "dataHora": "2026-09-26T19:02:11"
}
```

## Fluxo de uma pergunta (AtendimentoService)

1. Valida o usuário (404 se não existir).
2. Normaliza o texto (`"Vocês TROCAM tela??"` → `"voces trocam tela"`) e consulta o **cache** no banco
   (última resposta real para o mesmo texto normalizado dentro da validade).
3. Cache miss → monta o **contexto**: system prompt + últimas N interações do usuário.
4. Chama a IA. `IaException` → resposta de **fallback**, `origem = FALLBACK`, sistema segue no ar.
5. Persiste a `Pergunta` com origem, provedor, modelo, tokens e tempo.
6. Bônus: classifica a pergunta (orçamento / prazo / garantia / suporte / outros) com uma chamada mínima.

## Configuração

| Propriedade | Padrão | Observação |
|---|---|---|
| `ia.provedor` | `gemini` | `claude` ou `openai` também funcionam |
| `ia.cache.ativo` | `true` | desligue para medir o tempo sem cache |
| `ia.cache.validade-minutos` | `60` | validade da resposta em cache |
| `ia.historico.tamanho` | `6` | mensagens de contexto reenviadas (custo!) |
| `ia.max-tokens` | `500` | limite de saída |

## Kit inicial

`../aula07-kit-inicial` é este projeto com `AtendimentoService` e `CacheService` esvaziados (`TODO`).
