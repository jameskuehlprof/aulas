package br.com.senac.aula06.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import br.com.senac.aula06.visao.Rotulo;

/** DTOs de saida da API (a entrada e sempre multipart: arquivo + provedor). */
public final class Dtos {

    private Dtos() {}

    // ------------------------------------------------------------ Exercicio 1
    public record RotulosResponse(String provedor, String arquivo, long tempoMs, List<Rotulo> rotulos) {}

    // ------------------------------------------------------------ Exercicio 2
    public record OcrResponse(String provedor, String arquivo, long tempoMs, int palavras, String texto) {}

    public record ComparacaoOcrResponse(
            String provedor,
            OcrResponse imagemNitida,
            OcrResponse imagemRuim,
            double similaridadePercentual,
            String recomendacao) {}

    // ------------------------------------------------------------ Exercicio 3
    public record DescricaoProdutoResponse(String provedor, long tempoMs, String titulo, String descricao,
                                           List<String> tags, String categoria) {}

    // ------------------------------------------------------------ Desafio
    public record ReciboResponse(
            String provedor,
            long tempoMs,
            boolean legivel,
            String estabelecimento,
            LocalDate data,
            BigDecimal valorTotal,
            String formaPagamento,
            List<ItemRecibo> itens,
            List<String> avisos) {}

    public record ItemRecibo(String descricao, Integer quantidade, BigDecimal valor) {}

    // ------------------------------------------------------------ Info
    public record InfoResponse(List<String> provedores, String provedorPadrao, List<String> tiposAceitos, int tamanhoMaximoMb) {}
}
