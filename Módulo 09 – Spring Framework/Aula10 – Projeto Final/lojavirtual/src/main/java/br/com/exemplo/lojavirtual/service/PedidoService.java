package br.com.exemplo.lojavirtual.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.exemplo.lojavirtual.dto.ItemPedidoRequestDTO;
import br.com.exemplo.lojavirtual.dto.PedidoDTO;
import br.com.exemplo.lojavirtual.dto.PedidoRequestDTO;
import br.com.exemplo.lojavirtual.exception.RecursoNaoEncontradoException;
import br.com.exemplo.lojavirtual.mapper.PedidoMapper;
import br.com.exemplo.lojavirtual.model.Cliente;
import br.com.exemplo.lojavirtual.model.ItemPedido;
import br.com.exemplo.lojavirtual.model.Pedido;
import br.com.exemplo.lojavirtual.model.Produto;
import br.com.exemplo.lojavirtual.repository.PedidoRepository;

/**
 * @Transactional(readOnly = true) na classe: como Pedido tem relações LAZY
 * (cliente, itens.pedido), precisamos que a sessão do Hibernate continue
 * aberta enquanto montamos o PedidoDTO — é isso que a anotação garante.
 * Sem ela, dependeríamos do "Open Session in View" do Spring Boot (que vem
 * ligado por padrão, mas é considerado boa prática desligar em produção —
 * assunto da Aula 7).
 */
@Service
@Transactional(readOnly = true)
public class PedidoService {

    private final PedidoRepository repository;
    private final ClienteService clienteService;
    private final ProdutoService produtoService;

    public PedidoService(PedidoRepository repository, ClienteService clienteService, ProdutoService produtoService) {
        this.repository = repository;
        this.clienteService = clienteService;
        this.produtoService = produtoService;
    }

    public List<PedidoDTO> listarTodos() {
        return repository.findAll().stream().map(PedidoMapper::paraDTO).collect(Collectors.toList());
    }

    public List<PedidoDTO> listarPorCliente(Long clienteId) {
        return repository.findByClienteId(clienteId).stream().map(PedidoMapper::paraDTO).collect(Collectors.toList());
    }

    public PedidoDTO buscarPorId(Long id) {
        return PedidoMapper.paraDTO(buscarEntidade(id));
    }

    @Transactional
    public PedidoDTO criar(PedidoRequestDTO dto) {
        Cliente cliente = clienteService.buscarEntidade(dto.getClienteId());

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);

        for (ItemPedidoRequestDTO itemDto : dto.getItens()) {
            Produto produto = produtoService.buscarEntidade(itemDto.getProdutoId());
            // preco "congelado" no momento da compra: se o produto mudar de
            // preço amanhã, este pedido já feito continua com o valor daqui.
            ItemPedido item = new ItemPedido(produto, itemDto.getQuantidade(), produto.getPreco());
            pedido.adicionarItem(item);
        }

        return PedidoMapper.paraDTO(repository.save(pedido));
    }

    @Transactional
    public void deletar(Long id) {
        Pedido pedido = buscarEntidade(id);
        repository.delete(pedido); // cascade + orphanRemoval cuidam dos itens
    }

    private Pedido buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido não encontrado: id " + id));
    }
}
