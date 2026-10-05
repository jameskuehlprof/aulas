# Imagens de teste – Aula 06

| Arquivo | Para quê |
|---|---|
| `recibo-nitido.png` | Ex. 2 (OCR) e Desafio (recibo). Total R$ 100,00 · 19/09/2026 · PIX · 5 itens (soma 102,95 com desconto de 2,95 — bom para testar a validação "soma ≠ total") |
| `recibo-baixa-qualidade.png` | Ex. 2: mesmo recibo exportado em 200 px de largura (simula foto ruim). Use em `/api/visao/ocr/comparar` |
| `cartao-visita.png` | Ex. 2: OCR de cartão de visita (nome, telefone, e-mail, endereço) |
| `cena-mesa-cafe.jpg` | Jogo de abertura "Humano × Máquina" (ver `aula06-material-apoio.md`): mesa de café da manhã cheia de itens (ovos, waffles, morangos, mirtilos, café, suco, bule) — ótima para gerar muitos rótulos e comparar com o que a turma anotou. Foto de Rachel Park, [Unsplash](https://unsplash.com/pt-br/fotografias/cafe-da-manha-com-waffles-ovos-e-suco-hrlvr2ZlUNk), licença livre |

**Para o Exercício 1 (rótulos) e o Exercício 3 (produto), usem fotos reais** tiradas com o celular:
uma rua, um prato de comida, um objeto da mesa, um tênis, uma caneca. Envie pelo WhatsApp Web ou
cabo USB para a máquina do laboratório. Fotos reais deixam a comparação entre provedores muito mais
interessante do que imagens geradas.

**Para gerar uma "foto ruim" de qualquer imagem:** abra no Paint, redimensione para 15-20% e salve;
ou fotografe a tela do notebook com o celular tremendo de leve.

**Nunca usem fotos de rosto de colegas nem documentos reais (RG, CNH, cartão)** — LGPD e bom senso.
