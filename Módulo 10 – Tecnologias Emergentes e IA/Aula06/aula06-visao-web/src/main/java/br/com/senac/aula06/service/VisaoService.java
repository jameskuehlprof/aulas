package br.com.senac.aula06.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.senac.aula06.dto.Dtos.ComparacaoOcrResponse;
import br.com.senac.aula06.dto.Dtos.DescricaoProdutoResponse;
import br.com.senac.aula06.dto.Dtos.OcrResponse;
import br.com.senac.aula06.dto.Dtos.RotulosResponse;
import br.com.senac.aula06.visao.Imagem;
import br.com.senac.aula06.visao.VisaoException;
import br.com.senac.aula06.visao.VisaoProvider;
import br.com.senac.aula06.visao.VisaoProviderFactory;

/**
 * Regras de negocio dos Exercicios 1, 2 e 3.
 * (O Desafio - leitor de recibos - esta em ReciboService.)
 */
@Service
public class VisaoService {

    private final VisaoProviderFactory fabrica;
    private final ObjectMapper mapper;

    public VisaoService(VisaoProviderFactory fabrica, ObjectMapper mapper) {
        this.fabrica = fabrica;
        this.mapper = mapper;
    }

    // ------------------------------------------------------------ Exercicio 1
    public RotulosResponse rotulos(String provedor, Imagem imagem) {
        VisaoProvider p = fabrica.obter(provedor);
        long t = System.currentTimeMillis();
        var lista = p.rotulos(imagem);
        return new RotulosResponse(p.nome(), imagem.nomeArquivo(), System.currentTimeMillis() - t, lista);
    }

    // ------------------------------------------------------------ Exercicio 2
    public OcrResponse ocr(String provedor, Imagem imagem) {
        VisaoProvider p = fabrica.obter(provedor);
        long t = System.currentTimeMillis();
        String texto = p.ocr(imagem);
        return new OcrResponse(p.nome(), imagem.nomeArquivo(), System.currentTimeMillis() - t,
                contarPalavras(texto), texto);
    }

    /**
     * Roda o OCR nas duas imagens (nitida x ruim) e mede quantas palavras da
     * versao nitida "sobreviveram" na versao ruim (similaridade de Jaccard).
     */
    public ComparacaoOcrResponse compararOcr(String provedor, Imagem nitida, Imagem ruim) {
        OcrResponse a = ocr(provedor, nitida);
        OcrResponse b = ocr(provedor, ruim);
        double sim = similaridade(a.texto(), b.texto());
        String rec = sim >= 90 ? "Excelente: a foto de baixa qualidade ainda foi lida. Mesmo assim, padronize: foto de frente, boa luz, sem sombra."
                : sim >= 60 ? "Parcial: houve perda de palavras. Oriente o usuario: apoiar o documento em superficie plana, luz uniforme, sem flash direto."
                : "Ruim: OCR nao confiavel. Bloqueie o envio e peca nova foto (nitidez minima), ou use o app da camera com modo 'documento'.";
        return new ComparacaoOcrResponse(a.provedor(), a, b, sim, rec);
    }

    // ------------------------------------------------------------ Exercicio 3
    public DescricaoProdutoResponse descreverProduto(String provedor, Imagem imagem, String publicoAlvo) {
        VisaoProvider p = fabrica.obter(provedor);
        if (!p.suportaPromptLivre()) {
            throw new VisaoException(400, "Descricao de produto exige um modelo multimodal (gemini, claude ou openai).");
        }
        String prompt = """
                Voce e redator de um e-commerce brasileiro. Olhe a foto do produto e escreva:
                - "titulo": ate 60 caracteres, objetivo, com a caracteristica principal
                - "descricao": 2 paragrafos curtos, tom %s, sem inventar especificacoes que nao aparecem na foto
                - "tags": 5 palavras-chave para busca, minusculas
                - "categoria": uma categoria de loja (ex.: "calcados", "eletronicos", "casa e decoracao")
                Responda SOMENTE com um JSON com essas 4 chaves. Se a imagem nao for de um produto,
                responda {"titulo":"","descricao":"Imagem nao parece ser de um produto.","tags":[],"categoria":""}
                """.formatted(publicoAlvo == null || publicoAlvo.isBlank() ? "neutro e vendedor" : publicoAlvo);
        long t = System.currentTimeMillis();
        String resposta = p.perguntar(imagem, prompt);
        try {
            JsonNode n = mapper.readTree(limpar(resposta));
            List<String> tags = new java.util.ArrayList<>();
            n.path("tags").forEach(x -> tags.add(x.asText()));
            return new DescricaoProdutoResponse(p.nome(), System.currentTimeMillis() - t,
                    n.path("titulo").asText(""), n.path("descricao").asText(""), tags, n.path("categoria").asText(""));
        } catch (Exception e) {
            throw new VisaoException(502, "O modelo nao devolveu JSON valido. Resposta bruta: " + resposta);
        }
    }

    // ------------------------------------------------------------ utilitarios
    static int contarPalavras(String texto) {
        if (texto == null || texto.isBlank() || texto.contains("[SEM TEXTO]")) return 0;
        return texto.trim().split("\\s+").length;
    }

    static double similaridade(String a, String b) {
        Set<String> sa = palavras(a), sb = palavras(b);
        if (sa.isEmpty() && sb.isEmpty()) return 100.0;
        Set<String> inter = new HashSet<>(sa);
        inter.retainAll(sb);
        Set<String> uniao = new HashSet<>(sa);
        uniao.addAll(sb);
        return Math.round(inter.size() * 10000.0 / uniao.size()) / 100.0;
    }

    private static Set<String> palavras(String t) {
        if (t == null) return Set.of();
        return Arrays.stream(t.toLowerCase().replaceAll("[^\\p{L}\\p{N}\\s]", " ").split("\\s+"))
                .filter(w -> w.length() > 1).collect(Collectors.toSet());
    }

    static String limpar(String texto) {
        String t = texto.trim();
        if (t.startsWith("```")) t = t.replaceFirst("^```[a-zA-Z]*\\s*", "").replaceFirst("\\s*```$", "");
        int i = t.indexOf('{'), f = t.lastIndexOf('}');
        return i >= 0 && f > i ? t.substring(i, f + 1) : t;
    }
}
