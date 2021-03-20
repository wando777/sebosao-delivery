package br.com.cwi.reset.tcc.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
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
import br.com.cwi.reset.tcc.dominio.dto.ConsultarItemPedido;
import br.com.cwi.reset.tcc.dominio.dto.ConsultarPedidoDTO;
import br.com.cwi.reset.tcc.dominio.dto.ItemPedidoDTO;
import br.com.cwi.reset.tcc.dominio.dto.PedidoDTO;
import br.com.cwi.reset.tcc.dominio.dto.VisualizarPedidoDTO;
import br.com.cwi.reset.tcc.exceptions.FormaDePagamentoInvalidaException;
import br.com.cwi.reset.tcc.exceptions.HorarioInvalidoException;
import br.com.cwi.reset.tcc.exceptions.ObjetoNuloException;
import br.com.cwi.reset.tcc.exceptions.PedidoComStatusInvalidoException;
import br.com.cwi.reset.tcc.exceptions.ProdutoNaoPertenceAoEstabelecimentoException;
import br.com.cwi.reset.tcc.exceptions.QuantidadeMaximaDeProdutosExcedidaException;
import br.com.cwi.reset.tcc.exceptions.UsuarioSemEnderecoException;
import br.com.cwi.reset.tcc.repositories.EntregadorRepository;
import br.com.cwi.reset.tcc.repositories.PedidoRepository;
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

	public VisualizarPedidoDTO salvarProduto(@Valid PedidoDTO pedidoDto) {

		Estabelecimento estabelecimento = estabelecimentoService
				.buscarEstabelecimentoPorId(pedidoDto.getIdEstabelecimento());
		Usuario usuario = usuarioService.buscarUsuarioPorId(pedidoDto.getIdUsuarioSolicitante());
		Endereco endereco = enderecoService.buscarEnderecoPorUsuario(pedidoDto.getIdEnderecoEntrega(), usuario);

		List<Produto> produtos = new ArrayList<Produto>();
		List<ItemPedido> itens = new ArrayList<ItemPedido>();
		LocalDateTime horaDoPedido = LocalDateTime.now();
		Integer tempoPreparo = 0;
		BigDecimal valorTotal = new BigDecimal(0.0);

		for (ItemPedidoDTO p : pedidoDto.getItens()) {
			Produto produto = produtoService.buscarProdutoPorId(p.getIdProduto());
			ItemPedido item = new ItemPedido();
			if (produto.getEstabelecimento().getId().compareTo(estabelecimento.getId()) != 0) {
				throw new ProdutoNaoPertenceAoEstabelecimentoException(
						"O Produto " + produto.getId() + " Não pertence ao estebelecimento " + estabelecimento.getId());
			}
			if (p.getQuantidade() > QUANTIDADE_MAXIMA_DE_PRODUTOS) {
				throw new QuantidadeMaximaDeProdutosExcedidaException(
						"A quantidade máxima de produtos é " + QUANTIDADE_MAXIMA_DE_PRODUTOS);
			}
			tempoPreparo += p.getQuantidade() * produto.getTempoPreparo();
			valorTotal = valorTotal.add(produto.getValor().multiply(new BigDecimal(p.getQuantidade())));
			produtos.add(produto);
			// XXX Devo fazer um mapper pra isso também?
			item.setProduto(produto);
			item.setQuantidade(p.getQuantidade());
			itens.add(item);
		}
		LocalDateTime entrega = horaDoPedido.plusMinutes(tempoPreparo);
		validarPedido(estabelecimento, pedidoDto, usuario);

		Pedido pedido = PedidoMapper.mapearPedido(endereco, estabelecimento, pedidoDto, horaDoPedido, entrega, usuario,
				valorTotal, itens);
		pedidoRepository.save(pedido);

		VisualizarPedidoDTO visualizar = PedidoMapper.mapearVisualizacaoPedido(pedido.getId(), endereco,
				pedido.getStatus(), tempoPreparo, valorTotal);

		return visualizar;
	}

	public void cancelarPedido(Long id) {
		Pedido pedido = buscarPedido(id);
		validarCancelamento(pedido);
		pedido.setStatus(StatusPedido.CANCELADO);
		pedido.setHorarioCancelamento(LocalDateTime.now());
		pedidoRepository.save(pedido);
	}

	private Pedido buscarPedido(Long id) {
		Optional<Pedido> pedido = pedidoRepository.findById(id);
		if (pedido.isEmpty()) {
			throw new ObjetoNuloException("O pedido não existe");
		}
		return pedido.get();
	}

	private void validarCancelamento(Pedido pedido) {
		validarSatusEmPreparo(pedido);
		if (LocalDateTime.now().minusMinutes(10).isAfter(pedido.getHorarioSolicitacao())) {
			throw new HorarioInvalidoException("O pedido só pode ser cancelado em até 10 minutos");
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
				// FIXME TIRAR O MINUSHOUR() QUE FIZ PARA FUNCIONAR DEPOIS DAS 23 HRS
				if (DataUtils.horaAgora().minusHours(7l).isBefore(hr.getHorarioAbertura())
						|| DataUtils.horaAgora().minusHours(7l).isAfter(hr.getHorarioFechamento())) {
					throw new HorarioInvalidoException("O estabelecimento está fechado");
				}
			}
			;
		});
	}

	public ConsultarPedidoDTO buscarPedidoPorId(Long id) {
		Pedido pedido = buscarPedido(id);
		List<ConsultarItemPedido> itens = new ArrayList<ConsultarItemPedido>();
		pedido.getItensPedido().forEach(item -> {
			// XXX Devo por isso num mapper?
			ConsultarItemPedido itenSalvo = new ConsultarItemPedido();
			itenSalvo.setTitulo(item.getProduto().getTitulo());
			itenSalvo.setQuantidade(item.getQuantidade());
			itens.add(itenSalvo);
		});
		ConsultarPedidoDTO consultar = PedidoMapper.mapearConsultaPedido(pedido.getSolicitante().getNome(),
				pedido.getEnderecoEntrega(), pedido.getEstabelecimento().getNomeFantasia(), pedido.getValorTotal(),
				pedido.getEntregador(), pedido.getHorarioEntrega(), pedido.getStatus(), itens);
		return consultar;
	}

	public Entregador entregarPedido(Long id) {
		Pedido pedido = buscarPedido(id);
		validarSatusEmPreparo(pedido);
		pedido.setEntregador(entregadorService.getEntregadorDisponivel());
		pedido.setHorarioSaiuParaEntrega(LocalDateTime.now());
		pedido.setStatus(StatusPedido.SAIU_PARA_ENTREGA);
		pedido.getEntregador().setDisponivel(false);
		
		entregadorRepository.save(pedido.getEntregador());
		pedidoRepository.save(pedido);
		
		return pedido.getEntregador();
	}

	private void validarSatusEmPreparo(Pedido pedido) {
		if (!pedido.getStatus().equals(StatusPedido.EM_PREPARO)) {
			throw new PedidoComStatusInvalidoException("Este pedido não está em preparo e por isso não pode ser cancelado");
		}
	}

}
