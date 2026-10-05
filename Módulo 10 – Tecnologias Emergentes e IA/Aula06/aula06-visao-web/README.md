# Aula 06 – Visão Computacional em Java Web

Projeto Spring Boot de apoio à Aula 06 do Módulo 10. A API recebe **imagens por upload**
(multipart/form-data) e usa visão computacional para: identificar rótulos, extrair texto (OCR),
descrever um produto para e-commerce e ler recibos com dados estruturados e validados.

Dois tipos de provedor, de propósito:

| Tipo | Provedores | Como funciona | Quando usar |
|---|---|---|---|
| **Modelo multimodal** | `gemini`, `claude`, `openai` | imagem + **prompt** em linguagem natural | perguntas abertas, extração em JSON, descrição |
| **API especializada** | `google-vision` | você pede *features* (`LABEL_DETECTION`, `TEXT_DETECTION`) e recebe JSON com score | rótulos e OCR em volume, custo baixo e previsível |

```
src/main/java/br/com/senac/aula06
├── Aula06Application.java
├── config/VisaoProperties.java      # visao.* do application.properties
├── visao/
│   ├── VisaoProvider.java           # contrato: rotulos(), ocr(), perguntar()
│   ├── HttpJsonBase.java            # HttpClient + Jackson + erros (igual à Aula 05)
│   ├── MultimodalBase.java          # rótulos e OCR via prompt + parse de JSON
│   ├── GeminiVisao / ClaudeVisao / OpenAiVisao.java   # 3 formatos de "imagem no JSON"
│   ├── GoogleCloudVision.java       # images:annotate (API especializada)
│   ├── Imagem.java                  # validação de tipo/tamanho + base64
│   └── VisaoProviderFactory.java
├── service/VisaoService.java        # Ex. 1, 2 e 3
├── service/ReciboService.java       # Desafio: JSON estruturado + validação em Java
├── controller/VisaoController.java  # endpoints multipart
└── resources/static/index.html      # página de upload e teste
```

## Como rodar

1. Eclipse: File > Import > Maven > Existing Maven Projects → `aula06-visao-web`.
2. Run > Run Configurations > Environment: `GEMINI_API_KEY` (gratuita) e, se tiver,
   `ANTHROPIC_API_KEY`, `OPENAI_API_KEY`, `GOOGLE_VISION_API_KEY`.
3. Run `Aula06Application`. Acesse <http://localhost:8080/> (página) ou
   <http://localhost:8080/swagger-ui.html>.

> **Google Cloud Vision** exige projeto no Google Cloud com faturamento e a "Cloud Vision API"
> ativada (há cota gratuita de 1.000 unidades/mês). É opcional: a aula funciona só com o Gemini.

## Endpoints

| Método | Rota | Campos (multipart) | Exercício |
|---|---|---|---|
| GET | `/api/visao/info` | – | provedores e limites |
| POST | `/api/visao/rotulos` | `imagem`, `provedor?` | Ex. 1 |
| POST | `/api/visao/ocr` | `imagem`, `provedor?` | Ex. 2 |
| POST | `/api/visao/ocr/comparar` | `nitida`, `ruim`, `provedor?` | Ex. 2 |
| POST | `/api/visao/produto` | `imagem`, `provedor?`, `tom?` | Ex. 3 |
| POST | `/api/visao/recibo` | `imagem`, `provedor?` | Desafio |

Exemplo com curl:

```bash
curl -X POST "http://localhost:8080/api/visao/recibo?provedor=gemini" -F "imagem=@../imagens-teste/recibo-nitido.png"
```

Resposta (validada em Java — repare nos `avisos`):

```json
{
  "provedor": "gemini", "tempoMs": 2100, "legivel": true,
  "estabelecimento": "MERCADO BOM PRECO LTDA", "data": "2026-09-19",
  "valorTotal": 100.00, "formaPagamento": "pix",
  "itens": [ { "descricao": "ARROZ 5KG", "quantidade": 1, "valor": 24.90 }, "..." ],
  "avisos": [ "Soma dos itens (102.95) difere do total (100.00): pode haver desconto ou item nao lido. Confira." ]
}
```

## Como a imagem entra no JSON de cada provedor

| Provedor | Onde a imagem vai |
|---|---|
| Gemini | `contents[0].parts[0].inline_data = { mime_type, data(base64) }` |
| Claude | `messages[0].content[0] = { type:"image", source:{ type:"base64", media_type, data } }` |
| OpenAI | `messages[0].content[1] = { type:"image_url", image_url:{ url:"data:image/png;base64,..." } }` |
| Cloud Vision | `requests[0].image.content = base64` + `features[{type:"LABEL_DETECTION"}]` |

## Ética e LGPD (discutido em aula)

- O projeto **não usa reconhecimento facial** de propósito: rosto é dado biométrico (dado pessoal
  sensível na LGPD). Identificar pessoas exige base legal específica e consentimento.
- Fotos de documentos contêm CPF, endereço, etc.: não guarde a imagem nem o texto extraído além do
  necessário, e nunca envie para um provedor sem informar o usuário.

## Kit inicial

`../aula06-kit-inicial` é este mesmo projeto com `VisaoService` e `ReciboService` esvaziados para a turma implementar.
