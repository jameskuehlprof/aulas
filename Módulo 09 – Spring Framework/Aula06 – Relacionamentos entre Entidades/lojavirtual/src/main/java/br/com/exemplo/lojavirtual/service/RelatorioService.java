package br.com.exemplo.lojavirtual.service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.exemplo.lojavirtual.dto.RelatorioDTO;
import br.com.exemplo.lojavirtual.model.Categoria;
import br.com.exemplo.lojavirtual.model.ItemPedido;
import br.com.exemplo.lojavirtual.model.Pedido;
import br.com.exemplo.lojavirtual.repository.CategoriaRepository;
import br.com.exemplo.lojavirtual.repository.PedidoRepository;

/**
 * Exercicio da Aula 06: navegar pela cadeia de relacionamentos
 * Pedido -> ItemPedido -> Produto -> Categoria para somar, por categoria,
 * tudo que ja foi vendido. Nenhuma query nova - e o proprio grafo de
 * entidades (os @ManyToOne/@OneToMany do model) sendo percorrido em
 * memoria, de proposito, para deixar visivel o que cada relacionamento
 * esta entregando.
 *
 * Em um sistema real, com muitos pedidos, isso NAO seria feito assim -
 * seria uma query agregada (@Query com SUM/GROUP BY, ou uma projection).
 * Aqui o objetivo e didatico: ver a navegacao objeto-a-objeto funcionando
 * antes de otimizar.
 */
@Service
public class RelatorioService {

    private final CategoriaRepository categoriaRepository;
    private final PedidoRepository pedidoRepository;

    public RelatorioService(CategoriaRepository categoriaRepository, PedidoRepository pedidoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.pedidoRepository = pedidoRepository;
    }

    public List<RelatorioDTO> totalVendidoPorCategoria() {
        List<Pedido> pedidos = pedidoRepository.findAll();

        List<RelatorioDTO> relatorio = categoriaRepository.findAll().stream()
                .map(categoria -> new RelatorioDTO(
                        categoria.getId(),
                        categoria.getNome(),
                        totalDaCategoria(pedidos, categoria)))
                .collect(Collectors.toList());

        // Desafio extra do enunciado: maior total vendido primeiro.
        relatorio.sort(Comparator.comparing(RelatorioDTO::getTotalVendido).reversed());

        return relatorio;
    }

    private BigDecimal totalDaCategoria(List<Pedido> pedidos, Categoria categoria) {
        return pedidos.stream()
                .flatMap(pedido -> pedido.getItens().stream())
                .filter(item -> pertenceACategoria(item, categoria))
                .map(ItemPedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private boolean pertenceACategoria(ItemPedido item, Categoria categoria) {
        Categoria categoriaDoProduto = item.getProduto().getCategoria();
        return categoriaDoProduto != null && categoriaDoProduto.getId().equals(categoria.getId());
    }
}
