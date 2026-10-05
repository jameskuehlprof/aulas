package br.com.exemplo.lojavirtual.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import br.com.exemplo.lojavirtual.dto.ItemPedidoDTO;
import br.com.exemplo.lojavirtual.dto.PedidoDTO;
import br.com.exemplo.lojavirtual.model.ItemPedido;
import br.com.exemplo.lojavirtual.model.Pedido;

/**
 * Mesmo padrao do ProdutoMapper/ClienteMapper (Aula 7). Unico Mapper do
 * projeto que precisa de um metodo auxiliar: PedidoDTO carrega uma lista
 * de ItemPedidoDTO, entao paraDTO(ItemPedido) resolve a conversao de cada
 * item antes de paraDTO(Pedido) montar a lista e somar o total.
 */
public final class PedidoMapper {

    private PedidoMapper() {
    }

    public static PedidoDTO paraDTO(Pedido pedido) {
        List<ItemPedidoDTO> itensDTO = pedido.getItens().stream()
                .map(PedidoMapper::paraDTO)
                .collect(Collectors.toList());

        BigDecimal total = itensDTO.stream()
                .map(ItemPedidoDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new PedidoDTO(
                pedido.getId(),
                pedido.getCliente().getId(),
                pedido.getCliente().getNome(),
                pedido.getDataPedido(),
                itensDTO,
                total);
    }

    public static ItemPedidoDTO paraDTO(ItemPedido item) {
        return new ItemPedidoDTO(
                item.getProduto().getId(),
                item.getProduto().getNome(),
                item.getQuantidade(),
                item.getPrecoUnitario(),
                item.getSubtotal());
    }
}
