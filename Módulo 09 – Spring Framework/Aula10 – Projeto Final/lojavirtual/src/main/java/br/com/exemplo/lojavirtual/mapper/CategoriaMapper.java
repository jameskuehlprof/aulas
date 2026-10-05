package br.com.exemplo.lojavirtual.mapper;

import br.com.exemplo.lojavirtual.dto.CategoriaDTO;
import br.com.exemplo.lojavirtual.model.Categoria;

public final class CategoriaMapper {

    private CategoriaMapper() {
    }

    public static CategoriaDTO paraDTO(Categoria categoria) {
        return new CategoriaDTO(categoria.getId(), categoria.getNome());
    }
}
