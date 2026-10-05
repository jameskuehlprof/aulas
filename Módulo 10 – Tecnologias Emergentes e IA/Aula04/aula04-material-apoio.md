# Aula 04 – "Consumindo APIs de IA em Java" — material de apoio

Gerado com base nos arquivos já existentes na pasta (`Aula04 – Consumindo APIs de IA em Java.pptx`, o `.docx` do plano de aula e o `Exercícios e Desafios - Aula04.docx`) — não altera esses arquivos, é material complementar.

## Diagrama: `aula04-anatomia-rest-diagrama.png`
Três blocos, prontos para colar no slide 4 (Anatomia da Requisição REST) e no slide 6 (Primeira Chamada HTTP):
1. **Mesma estrutura, dois cenários** — comparação lado a lado entre a chamada que a turma roda em sala (GET, agify.io, sem autenticação) e uma chamada de IA de verdade (POST, com `Authorization: Bearer <chave>`), mostrando que o formato é idêntico, só o conteúdo muda.
2. **API Key certo vs. errado** — chave hardcoded no código (❌, vaza no Git) vs. lida por `System.getenv()` (✅).
3. **O caminho no código Java** — pipeline visual HttpClient → HttpRequest.newBuilder() → .send() → Jackson ObjectMapper, ligando com a tabela de status HTTP do slide 5.

## Códigos Java prontos

### `PrimeiraChamadaHttp.java` — demonstração ao vivo (slide 6, sem chave)
Implementa exatamente o passo a passo do slide 6: HttpClient + HttpRequest GET para `api.agify.io`, imprime o JSON bruto no console e extrai o campo `age` com Jackson. Roda direto no Eclipse, sem nenhuma configuração de chave — ideal para abrir a aula e já ver funcionando.

### `GeradorTextoIA.java` — gabarito de referência do Desafio
Implementação de referência para o Desafio do docx de exercícios ("Gerador automático de texto resiliente"). Cobre os 5 requisitos pedidos:
- Lê o prompt do usuário via terminal (`Scanner`)
- Monta e envia a requisição POST com o prompt em JSON (usa a API de Chat da OpenAI como exemplo, mesmo padrão `Authorization: Bearer <chave>` do slide 4)
- Exibe a resposta formatada
- Trata 401 (key inválida), 429 (limite excedido), 400 e timeout, cada um com mensagem específica (nunca mostra stack trace cru)
- Implementa 1 retry automático em caso de timeout

Para usar de verdade em sala, é preciso configurar a variável de ambiente `OPENAI_API_KEY` com uma chave de teste (Eclipse: Run > Run Configurations > Environment). Se preferir usar a API do Claude em vez da OpenAI, a troca é só a URL e o header de autenticação — o Claude usa `x-api-key`, não `Authorization: Bearer` — e isso em si é um bom gancho em sala para mostrar que nem toda API segue exatamente o mesmo padrão de autenticação, mesmo dentro da mesma categoria (IA Generativa).

Dependência Maven necessária para os dois arquivos:
```xml
<dependency>
  <groupId>com.fasterxml.jackson.core</groupId>
  <artifactId>jackson-databind</artifactId>
  <version>2.17.0</version>
</dependency>
```

## Pendências para o James
- Confirmar se a turma vai usar uma chave de teste real (OpenAI ou Anthropic) para o Desafio, ou se prefere fornecer uma API mock — o `GeradorTextoIA.java` está pronto para os dois casos, só muda a URL/headers se for mock.
- Testar o `GeradorTextoIA.java` com uma chave de teste antes da aula, para confirmar que o modelo `gpt-4o-mini` (ou o que for usado) ainda está disponível e o formato de resposta não mudou.
- Revisar se o valor da versão do Jackson (2.17.0) bate com o que já está configurado no `pom.xml` do projeto-base do módulo.
