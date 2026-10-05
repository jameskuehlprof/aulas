package br.com.senac.aula06.visao;

import java.util.Base64;
import java.util.Set;

/**
 * A imagem enviada pelo usuario, ja validada.
 *
 * Todas as APIs de visao recebem a imagem como texto base64 dentro do JSON
 * (uma foto de 2 MB vira ~2,7 MB de texto). Por isso limitamos o tamanho.
 */
public record Imagem(byte[] bytes, String mimeType, String nomeArquivo) {

    public static final Set<String> TIPOS_ACEITOS = Set.of("image/jpeg", "image/png", "image/webp", "image/gif");
    public static final int TAMANHO_MAXIMO = 8 * 1024 * 1024;

    public static Imagem de(byte[] bytes, String mimeType, String nomeArquivo) {
        if (bytes == null || bytes.length == 0) {
            throw new VisaoException(400, "Nenhuma imagem foi enviada.");
        }
        if (bytes.length > TAMANHO_MAXIMO) {
            throw new VisaoException(413, "Imagem muito grande (" + bytes.length / 1024 / 1024
                    + " MB). Limite: 8 MB. Reduza a resolucao da foto.");
        }
        if (mimeType == null || !TIPOS_ACEITOS.contains(mimeType.toLowerCase())) {
            throw new VisaoException(415, "Tipo de arquivo nao suportado: " + mimeType
                    + ". Envie JPEG, PNG, WEBP ou GIF.");
        }
        return new Imagem(bytes, mimeType.toLowerCase(), nomeArquivo);
    }

    public String base64() {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public String dataUrl() {
        return "data:" + mimeType + ";base64," + base64();
    }
}
