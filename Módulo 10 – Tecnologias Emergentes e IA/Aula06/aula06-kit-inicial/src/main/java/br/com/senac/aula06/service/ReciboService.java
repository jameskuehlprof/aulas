package br.com.senac.aula06.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.senac.aula06.dto.Dtos.ItemRecibo;
import br.com.senac.aula06.dto.Dtos.ReciboResponse;
import br.com.senac.aula06.visao.Imagem;
import br.com.senac.aula06.visao.VisaoException;
import br.com.senac.aula06.visao.VisaoProvider;
import br.com.senac.aula06.visao.VisaoProviderFactory;

/**
 * DESAFIO - Leitor automatico de recibos / notas fiscais.
 *
 * Fluxo:
 *   1) pede ao modelo multimodal um JSON com campos fixos (prompt com "schema")
 *   2) limpa e faz o parse do JSON
 *   3) VALIDA cada campo em Java (valor numerico > 0, data plausivel)
 *   4) nunca quebra: imagem ilegivel vira legivel=false + aviso claro
 *
 * Licao central: a IA extrai, mas quem GARANTE e o codigo. Nunca grave no
 * financeiro um valor que veio da IA sem validar.
 */
@Service
public class ReciboService {

    private static final String PROMPT = """
            Voce e um sistema de leitura de recibos e notas fiscais brasileiras.
            Extraia os dados da imagem e responda SOMENTE com um JSON neste formato:
            {
              "legivel": true,
              "estabelecimento": "nome da loja ou empresa",
              "data": "AAAA-MM-DD",
              "valorTotal": 123.45,
              "formaPagamento": "pix | cartao | dinheiro | desconhecido",
              "itens": [ {"descricao": "...", "quantidade": 1, "valor": 12.34} ]   // valor = total da linha
            }
            Regras:
            - valorTotal e um numero com ponto decimal, sem "R$" (ex.: 1234.50)
            - se um campo nao estiver visivel, use null
            - se a imagem NAO for um recibo/nota ou estiver ilegivel, responda
              {"legivel": false} e nada mais
            - em "itens", "valor" e o TOTAL da linha (ja multiplicado pela quantidade)
            - nao invente valores: prefira null a chutar
            """;

    private final VisaoProviderFactory fabrica;
    private final ObjectMapper mapper;

    public ReciboService(VisaoProviderFactory fabrica, ObjectMapper mapper) {
        this.fabrica = fabrica;
        this.mapper = mapper;
    }

    public ReciboResponse ler(String provedor, Imagem imagem) {
        VisaoProvider p = fabrica.obter(provedor);
        if (!p.suportaPromptLivre()) {
            throw new VisaoException(400, "Leitura de recibo exige um modelo multimodal (gemini, claude ou openai). "
                    + "Com o google-vision voce teria o OCR bruto e precisaria extrair os campos com regex.");
        }

        long t = System.currentTimeMillis();
        String bruto = p.perguntar(imagem, PROMPT);
        long tempo = System.currentTimeMillis() - t;

        JsonNode n;
        try {
            n = mapper.readTree(VisaoService.limpar(bruto));
        } catch (Exception e) {
            return ilegivel(p.nome(), tempo, "O modelo nao devolveu JSON. Resposta: " + resumo(bruto));
        }

        if (!n.path("legivel").asBoolean(false)) {
            return ilegivel(p.nome(), tempo, "Imagem ilegivel ou nao parece ser um recibo. Tire outra foto com boa luz.");
        }

        List<String> avisos = new ArrayList<>();

        BigDecimal total = validarValor(n.path("valorTotal"), "valorTotal", avisos);
        LocalDate data = validarData(n.path("data").asText(null), avisos);
        String estabelecimento = n.path("estabelecimento").asText(null);
        if (estabelecimento == null || estabelecimento.isBlank()) {
            avisos.add("Estabelecimento nao identificado.");
        }

        List<ItemRecibo> itens = new ArrayList<>();
        BigDecimal somaItens = BigDecimal.ZERO;
        for (JsonNode it : n.path("itens")) {
            BigDecimal v = validarValor(it.path("valor"), "item '" + it.path("descricao").asText("?") + "'", avisos);
            Integer q = it.hasNonNull("quantidade") ? it.path("quantidade").asInt() : null;
            itens.add(new ItemRecibo(it.path("descricao").asText(""), q, v));
            if (v != null) somaItens = somaItens.add(v);
        }
        if (total != null && !itens.isEmpty() && somaItens.signum() > 0
                && total.subtract(somaItens).abs().compareTo(new BigDecimal("0.05")) > 0) {
            avisos.add("Soma dos itens (" + somaItens + ") difere do total (" + total + "): pode haver desconto ou item nao lido. Confira.");
        }

        boolean legivel = total != null; // sem valor total, o recibo nao serve para o financeiro
        if (!legivel) avisos.add("Valor total nao encontrado: recibo marcado como ilegivel.");

        return new ReciboResponse(p.nome(), tempo, legivel, estabelecimento, data, total,
                n.path("formaPagamento").asText("desconhecido"), itens, avisos);
    }

    // ------------------------------------------------------------ validacoes
    static BigDecimal validarValor(JsonNode no, String campo, List<String> avisos) {
        if (no == null || no.isNull() || no.isMissingNode()) return null;
        String s = no.asText().replace("R$", "").replace(" ", "").trim();
        // aceita "1.234,56" (BR) e "1234.56" (JSON)
        if (s.matches("\\d{1,3}(\\.\\d{3})*,\\d{2}")) s = s.replace(".", "").replace(",", ".");
        else if (s.matches("\\d+,\\d{1,2}")) s = s.replace(",", ".");
        try {
            BigDecimal v = new BigDecimal(s);
            if (v.signum() <= 0) { avisos.add(campo + ": valor nao positivo (" + v + ")."); return null; }
            if (v.compareTo(new BigDecimal("100000")) > 0) avisos.add(campo + ": valor suspeito (" + v + "), confira.");
            return v.setScale(2, java.math.RoundingMode.HALF_UP);
        } catch (NumberFormatException e) {
            avisos.add(campo + ": nao e um numero valido ('" + no.asText() + "').");
            return null;
        }
    }

    static LocalDate validarData(String s, List<String> avisos) {
        if (s == null || s.isBlank() || "null".equals(s)) { avisos.add("Data nao identificada."); return null; }
        for (DateTimeFormatter f : List.of(DateTimeFormatter.ISO_LOCAL_DATE, DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                DateTimeFormatter.ofPattern("dd/MM/yy"))) {
            try {
                LocalDate d = LocalDate.parse(s.trim(), f);
                if (d.isAfter(LocalDate.now().plusDays(1))) avisos.add("Data no futuro (" + d + "), confira.");
                if (d.isBefore(LocalDate.now().minusYears(5))) avisos.add("Data muito antiga (" + d + "), confira.");
                return d;
            } catch (DateTimeParseException ignored) { }
        }
        avisos.add("Data em formato desconhecido: '" + s + "'.");
        return null;
    }

    private static ReciboResponse ilegivel(String provedor, long tempo, String motivo) {
        return new ReciboResponse(provedor, tempo, false, null, null, null, null, List.of(), List.of(motivo));
    }

    private static String resumo(String s) {
        return s.length() > 200 ? s.substring(0, 200) + "..." : s;
    }
}
