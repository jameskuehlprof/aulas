package br.com.exemplo.lojavirtual.mapper;

import br.com.exemplo.lojavirtual.dto.ProdutoDTO;
import br.com.exemplo.lojavirtual.model.Categoria;
import br.com.exemplo.lojavirtual.model.Produto;

/**
 * Camada MAPPER: converte entre Entity e DTO. Até a Aula 6, esse código
 * vivia espalhado dentro de cada Service (um método privado "paraDTO").
 * Extrair para uma classe própria:
 *
 *  - evita repetir a mesma conversão em mais de um lugar (ex.: se amanhã
 *    outro Service também precisar transformar Produto em ProdutoDTO);
 *  - deixa o Service focado só em regra de negócio, não em "tradução" de
 *    objetos;
 *  - facilita testar a conversão isoladamente.
 *
 * Métodos static: um Mapper normalmente não guarda estado, então não
 * precisa ser um @Component gerenciado pelo Spring (embora pudesse ser).
 */
public final class ProdutoMapper {

    private ProdutoMapper() {
    }

    public static ProdutoDTO paraDTO(Produto produto) {
        Categoria categoria = produto.getCategoria();
        return new ProdutoDTO(
                produto.getId(), produto.getNome(), produto.getPreco(),
                produto.getQuantidadeEmEstoque(), produto.getEstoqueMinimo(),
                categoria != null ? categoria.getId() : null,
                categoria != null ? categoria.getNome() : null);
    }
}
