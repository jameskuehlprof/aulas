package br.com.exemplo.lojavirtual.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import br.com.exemplo.lojavirtual.model.Produto;

/**
 * Camada REPOSITORY: responsável por "guardar e buscar" os dados.
 *
 * Nesta aula o repositório é apenas uma lista em memória (os dados somem
 * quando a aplicação reinicia). A partir da Aula 04, esta classe será
 * substituída por uma interface que estende JpaRepository, e o Spring Data
 * JPA passa a cuidar de tudo isso conversando com um banco de dados real.
 *
 * @Repository diz ao Spring que esta classe é um "bean" da camada de
 * persistência — assim como @Service e @Controller, é um tipo de
 * @Component especializado.
 */
@Repository
public class ProdutoRepository {

    private final List<Produto> produtos = new ArrayList<>();
    private final AtomicLong proximoId = new AtomicLong(1);

    public ProdutoRepository() {
        // Alguns produtos de exemplo, só para termos dados ao rodar a aplicação.
        produtos.add(new Produto(proximoId.getAndIncrement(), "Notebook", new java.math.BigDecimal("3500.00"), 10));
        produtos.add(new Produto(proximoId.getAndIncrement(), "Mouse", new java.math.BigDecimal("59.90"), 50));
        produtos.add(new Produto(proximoId.getAndIncrement(), "Teclado Mecânico", new java.math.BigDecimal("289.00"), 25));
    }

    public List<Produto> findAll() {
        return produtos;
    }

    public Optional<Produto> findById(Long id) {
        return produtos.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    public Produto save(Produto produto) {
        if (produto.getId() == null) {
            produto.setId(proximoId.getAndIncrement());
            produtos.add(produto);
        } else {
            produtos.removeIf(p -> p.getId().equals(produto.getId()));
            produtos.add(produto);
        }
        return produto;
    }

    public void deleteById(Long id) {
        produtos.removeIf(p -> p.getId().equals(id));
    }
}
