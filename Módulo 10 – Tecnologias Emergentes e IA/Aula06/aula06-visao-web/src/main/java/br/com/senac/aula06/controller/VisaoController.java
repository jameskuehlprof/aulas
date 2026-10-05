package br.com.senac.aula06.controller;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.senac.aula06.config.VisaoProperties;
import br.com.senac.aula06.dto.Dtos.ComparacaoOcrResponse;
import br.com.senac.aula06.dto.Dtos.DescricaoProdutoResponse;
import br.com.senac.aula06.dto.Dtos.InfoResponse;
import br.com.senac.aula06.dto.Dtos.OcrResponse;
import br.com.senac.aula06.dto.Dtos.ReciboResponse;
import br.com.senac.aula06.dto.Dtos.RotulosResponse;
import br.com.senac.aula06.service.ReciboService;
import br.com.senac.aula06.service.VisaoService;
import br.com.senac.aula06.visao.Imagem;
import br.com.senac.aula06.visao.VisaoException;
import br.com.senac.aula06.visao.VisaoProviderFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Todos os endpoints recebem a imagem por UPLOAD (multipart/form-data), como
 * um formulario web faria - por isso o Swagger mostra um botao "Choose File".
 *
 * Campo "provedor": gemini | claude | openai | google-vision (vazio = padrao).
 */
@RestController
@RequestMapping("/api/visao")
@Tag(name = "Visao Computacional", description = "Rotulos, OCR, descricao de produto e leitura de recibos")
public class VisaoController {

    private final VisaoService visao;
    private final ReciboService recibo;
    private final VisaoProviderFactory fabrica;
    private final VisaoProperties props;

    public VisaoController(VisaoService visao, ReciboService recibo, VisaoProviderFactory fabrica, VisaoProperties props) {
        this.visao = visao;
        this.recibo = recibo;
        this.fabrica = fabrica;
        this.props = props;
    }

    @GetMapping("/info")
    @Operation(summary = "Provedores disponiveis e limites de upload")
    public InfoResponse info() {
        return new InfoResponse(fabrica.nomes(), props.provedorPadrao(),
                Imagem.TIPOS_ACEITOS.stream().sorted().toList(), Imagem.TAMANHO_MAXIMO / 1024 / 1024);
    }

    @PostMapping(value = "/rotulos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Exercicio 1 - o que aparece na imagem? (ate 10 rotulos com confianca)")
    public RotulosResponse rotulos(
            @RequestPart("imagem") MultipartFile arquivo,
            @Parameter(description = "gemini | claude | openai | google-vision") @RequestParam(required = false) String provedor) {
        return visao.rotulos(provedor, converter(arquivo));
    }

    @PostMapping(value = "/ocr", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Exercicio 2 - extrai o texto da imagem (OCR)")
    public OcrResponse ocr(@RequestPart("imagem") MultipartFile arquivo,
                           @RequestParam(required = false) String provedor) {
        return visao.ocr(provedor, converter(arquivo));
    }

    @PostMapping(value = "/ocr/comparar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Exercicio 2 - compara o OCR de uma foto nitida com uma de baixa qualidade")
    public ComparacaoOcrResponse compararOcr(@RequestPart("nitida") MultipartFile nitida,
                                             @RequestPart("ruim") MultipartFile ruim,
                                             @RequestParam(required = false) String provedor) {
        return visao.compararOcr(provedor, converter(nitida), converter(ruim));
    }

    @PostMapping(value = "/produto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Exercicio 3 - gera titulo, descricao e tags de e-commerce a partir da foto")
    public DescricaoProdutoResponse produto(@RequestPart("imagem") MultipartFile arquivo,
                                            @RequestParam(required = false) String provedor,
                                            @Parameter(description = "ex.: 'jovem e descontraido', 'tecnico'") @RequestParam(required = false) String tom) {
        return visao.descreverProduto(provedor, converter(arquivo), tom);
    }

    @PostMapping(value = "/recibo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Desafio - le um recibo/nota e devolve dados estruturados e validados")
    public ReciboResponse recibo(@RequestPart("imagem") MultipartFile arquivo,
                                 @RequestParam(required = false) String provedor) {
        return recibo.ler(provedor, converter(arquivo));
    }

    /** MultipartFile (Spring) -> Imagem (nosso dominio), com validacao. */
    private static Imagem converter(MultipartFile arquivo) {
        try {
            return Imagem.de(arquivo.getBytes(), arquivo.getContentType(), arquivo.getOriginalFilename());
        } catch (IOException e) {
            throw new VisaoException(400, "Nao foi possivel ler o arquivo enviado.");
        }
    }
}
