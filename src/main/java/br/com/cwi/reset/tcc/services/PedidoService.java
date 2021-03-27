package br.com.cwi.reset.tcc.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cwi.reset.tcc.dominio.Endereco;
import br.com.cwi.reset.tcc.dominio.Entregador;
import br.com.cwi.reset.tcc.dominio.Estabelecimento;
import br.com.cwi.reset.tcc.dominio.FormaPagamento;
import br.com.cwi.reset.tcc.dominio.ItemPedido;
import br.com.cwi.reset.tcc.dominio.Pedido;
import br.com.cwi.reset.tcc.dominio.Produto;
import br.com.cwi.reset.tcc.dominio.StatusPedido;
import br.com.cwi.reset.tcc.dominio.Usuario;
import br.com.cwi.reset.tcc.dominio.dto.ConsultarItemPedidoDTO;
import br.com.cwi.reset.tcc.dominio.dto.ConsultarPedidoDTO;
import br.com.cwi.reset.tcc.dominio.dto.ItemPedidoDTO;
import br.com.cwi.reset.tcc.dominio.dto.PedidoDTO;
import br.com.cwi.reset.tcc.dominio.dto.VisualizarEntregadorDTO;
import br.com.cwi.reset.tcc.dominio.dto.VisualizarPedidoDTO;
import br.com.cwi.reset.tcc.exceptions.FormaDePagamentoInvalidaException;
import br.com.cwi.reset.tcc.exceptions.HorarioInvalidoException;
import br.com.cwi.reset.tcc.exceptions.ObjetoNullException;
import br.com.cwi.reset.tcc.exceptions.PedidoComStatusInvalidoException;
import br.com.cwi.reset.tcc.exceptions.ProdutoNaoPertenceAoEstabelecimentoException;
import br.com.cwi.reset.tcc.exceptions.QuantidadeMaximaDeProdutosExcedidaException;
import br.com.cwi.reset.tcc.exceptions.UsuarioSemEnderecoException;
import br.com.cwi.reset.tcc.repositories.EntregadorRepository;
import br.com.cwi.reset.tcc.repositories.PedidoRepository;
import br.com.cwi.reset.tcc.services.mappers.ConsultarItemPedidoMapper;
import br.com.cwi.reset.tcc.services.mappers.EntregadorMapper;
import br.com.cwi.reset.tcc.services.mappers.PedidoMapper;
import br.com.cwi.reset.tcc.utils.DataUtils;

@Service
public class PedidoService {

	private static final Integer QUANTIDADE_MAXIMA_DE_PRODUTOS = 5;

	@Autowired
	private PedidoRepository pedidoRepository;

	@Autowired
	private EntregadorRepository entregadorRepository;

	@Autowired
	private EstabelecimentoService estabelecimentoService;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private EnderecoService enderecoService;

	@Autowired
	private ProdutoService produtoService;

	@Autowired
	private EntregadorService entregadorService;

	private Integer tempoPreparo = 0;

	public VisualizarPedidoDTO salvarProduto(@Valid PedidoDTO pedidoDto) {

		Estabelecimento estabelecimento = estabelecimentoService
				.buscarEstabelecimentoPorId(pedidoDto.getIdEstabelecimento());
		Usuario usuario = usuarioService.buscarUsuarioPorId(pedidoDto.getIdUsuarioSolicitante());
		Endereco endereco = enderecoService.buscarEnderecoPorUsuario(pedidoDto.getIdEnderecoEntrega(), usuario);

		validarPedido(estabelecimento, pedidoDto, usuario);

		List<Produto> produtos = new ArrayList<Produto>();
		List<ItemPedido> itens = new ArrayList<ItemPedido>();
		LocalDateTime horaDoPedido = LocalDateTime.now();
		// Integer tempoPreparo = 0;
		BigDecimal valorTotal = new BigDecimal(0.0);

		for (ItemPedidoDTO p : pedidoDto.getItens()) {
			Produto produto = produtoService.buscarProdutoPorId(p.getIdProduto());
			ItemPedido item = new ItemPedido();
			validarPedido(estabelecimento, p, produto);
			tempoPreparo += p.getQuantidade() * produto.getTempoPreparo();
			valorTotal = valorTotal.add(produto.getValor().multiply(new BigDecimal(p.getQuantidade())));
			produtos.add(produto);
			item.setProduto(produto);
			item.setQuantidade(p.getQuantidade());
			itens.add(item);
		}

		// LocalDateTime entrega = horaDoPedido.plusMinutes(tempoPreparo);

		Pedido pedido = PedidoMapper.mapearPedido(endereco, estabelecimento, pedidoDto, horaDoPedido, usuario,
				valorTotal, itens);
		pedidoRepository.save(pedido);

		VisualizarPedidoDTO visualizar = PedidoMapper.mapearVisualizacaoPedido(pedido.getId(), endereco,
				pedido.getStatus(), tempoPreparo, valorTotal);

		return visualizar;
	}

	public void cancelarPedido(Long id) {
		Pedido pedido = buscarPedido(id);
		validarCancelamento(pedido);
		pedido = PedidoMapper.mapearCancelarPedido(pedido);
		pedidoRepository.save(pedido);
	}

	private Pedido buscarPedido(Long id) {
		Optional<Pedido> pedido = pedidoRepository.findById(id);
		if (pedido.isEmpty()) {
			throw new ObjetoNullException("O pedido não existe");
		}
		return pedido.get();
	}

	private void validarCancelamento(Pedido pedido) {
		validarSatusEmPreparo(pedido);
		if (LocalDateTime.now().minusMinutes(10).isAfter(pedido.getHorarioSolicitacao())) {
			throw new HorarioInvalidoException("O pedido só pode ser cancelado em até 10 minutos");
		}
	}

	public ConsultarPedidoDTO buscarPedidoPorId(Long id) {
		Pedido pedido = buscarPedido(id);
		// Integer tempoPreparo = 0;
		List<ConsultarItemPedidoDTO> itens = new ArrayList<ConsultarItemPedidoDTO>();
		pedido.getItensPedido().forEach(item -> {
			ConsultarItemPedidoDTO itenSalvo = ConsultarItemPedidoMapper.mapearConsultarItemPedido(item,
					new ConsultarItemPedidoDTO());
			tempoPreparo += item.getQuantidade() * item.getProduto().getTempoPreparo();
			itens.add(itenSalvo);
		});
		LocalDateTime entrega = pedido.getStatus() == StatusPedido.ENTREGUE ? null
				: pedido.getHorarioSolicitacao().plusMinutes(tempoPreparo);
		ConsultarPedidoDTO consultar = PedidoMapper.mapearConsultaPedido(pedido.getSolicitante().getNome(),
				pedido.getEnderecoEntrega(), pedido.getEstabelecimento().getNomeFantasia(), pedido.getValorTotal(),
				pedido.getEntregador(), entrega, pedido.getStatus(), itens);
		return consultar;
	}

	public VisualizarEntregadorDTO entregarPedido(Long id) {
		Pedido pedido = buscarPedido(id);
		validarSatusEmPreparo(pedido);
		pedido = PedidoMapper.mapearEntregarPedido(pedido, entregadorService.getEntregadorDisponivel());
		entregadorRepository.save(pedido.getEntregador());
		pedidoRepository.save(pedido);

		return EntregadorMapper.mapearVisualizarEntregador(pedido.getEntregador());
	}

	public void finalizarPedido(Long id) {
		Pedido pedido = buscarPedido(id);
		validarSatusSaiuPraEntrega(pedido);
		pedido = PedidoMapper.mapearFinalizarPedido(pedido);
		entregadorRepository.save(pedido.getEntregador());
		pedidoRepository.save(pedido);
	}

	private void validarSatusEmPreparo(Pedido pedido) {
		if (!pedido.getStatus().equals(StatusPedido.EM_PREPARO)) {
			throw new PedidoComStatusInvalidoException(
					"Este pedido não está em preparo. " + pedido.getStatus().getDescricao());
		}
	}

	private void validarSatusSaiuPraEntrega(Pedido pedido) {
		if (!pedido.getStatus().equals(StatusPedido.SAIU_PARA_ENTREGA)) {
			throw new PedidoComStatusInvalidoException(
					"Este pedido não saiu para entrega. " + pedido.getStatus().getDescricao());
		}
	}

	private void validarPedido(Estabelecimento estabelecimento, PedidoDTO pedidoDto, Usuario usuario) {
		FormaPagamento formaPagamento = pedidoDto.getFormaPagamento();
		if (!estabelecimento.getFormasPagamento().contains(formaPagamento)) {
			throw new FormaDePagamentoInvalidaException("Esse estabelecimento não aceita essa forma de pagamento");
		}
		if (usuario.getEnderecos().isEmpty()) {
			throw new UsuarioSemEnderecoException("O usuário não possui endereços cadastrados");
		}
		estabelecimento.getHorariosFuncionamento().forEach(hr -> {
			if (hr.getDiaSemana().equals(DataUtils.diaDaSemanaHoje())) {
				if (DataUtils.horaAgora().isBefore(hr.getHorarioAbertura())
						|| DataUtils.horaAgora().isAfter(hr.getHorarioFechamento())) {
					throw new HorarioInvalidoException("O estabelecimento está fechado");
				}
			}
			;
		});
	}

	private void validarPedido(Estabelecimento estabelecimento, ItemPedidoDTO p, Produto produto) {
		if (produto.getEstabelecimento().getId().compareTo(estabelecimento.getId()) != 0) {
			throw new ProdutoNaoPertenceAoEstabelecimentoException(
					"O Produto " + produto.getId() + " Não pertence ao estebelecimento " + estabelecimento.getId());
		}
		if (p.getQuantidade() > QUANTIDADE_MAXIMA_DE_PRODUTOS) {
			throw new QuantidadeMaximaDeProdutosExcedidaException(
					"A quantidade máxima de produtos é " + QUANTIDADE_MAXIMA_DE_PRODUTOS);
		}
	}

}
