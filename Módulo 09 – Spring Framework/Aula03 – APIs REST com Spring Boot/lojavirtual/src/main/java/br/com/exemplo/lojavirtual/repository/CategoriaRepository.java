package br.com.exemplo.lojavirtual.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import br.com.exemplo.lojavirtual.model.Categoria;

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
public class CategoriaRepository {

    private final List<Categoria> categorias = new ArrayList<>();
    private final AtomicLong proximoId = new AtomicLong(1);

    public CategoriaRepository() {
        // Algumas categorias de exemplo, só para termos dados ao rodar a aplicação.
        categorias.add(new Categoria(proximoId.getAndIncrement(), "Informática"));
        categorias.add(new Categoria(proximoId.getAndIncrement(), "Suprimentos"));
    }

    public List<Categoria> findAll() {
        return categorias;
    }

    public Optional<Categoria> findById(Long id) {
        return categorias.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    public Categoria save(Categoria categoria) {
        if (categoria.getId() == null) {
            categoria.setId(proximoId.getAndIncrement());
            categorias.add(categoria);
        } else {
        	categorias.removeIf(p -> p.getId().equals(categoria.getId()));
        	categorias.add(categoria);
        }
        return categoria;
    }

    public void deleteById(Long id) {
    	categorias.removeIf(p -> p.getId().equals(id));
    }
}
