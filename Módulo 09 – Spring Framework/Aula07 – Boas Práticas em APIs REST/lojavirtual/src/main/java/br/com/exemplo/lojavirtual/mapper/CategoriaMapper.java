package br.com.exemplo.lojavirtual.mapper;

import br.com.exemplo.lojavirtual.dto.CategoriaDTO;
import br.com.exemplo.lojavirtual.model.Categoria;

/**
 * Mesmo padrao do ProdutoMapper/ClienteMapper (Aula 7): tira do
 * CategoriaService a responsabilidade de converter Entity -> DTO.
 */
public final class CategoriaMapper {

    private CategoriaMapper() {
    }

    public static CategoriaDTO paraDTO(Categoria categoria) {
        return new CategoriaDTO(categoria.getId(), categoria.getNome());
    }
}
