package br.com.cwi.reset.tcc.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cwi.reset.tcc.dominio.Endereco;
import br.com.cwi.reset.tcc.dominio.Estabelecimento;
import br.com.cwi.reset.tcc.dominio.FormaPagamento;
import br.com.cwi.reset.tcc.dominio.Pedido;
import br.com.cwi.reset.tcc.dominio.Produto;
import br.com.cwi.reset.tcc.dominio.Usuario;
import br.com.cwi.reset.tcc.dominio.dto.ItemPedidoDTO;
import br.com.cwi.reset.tcc.dominio.dto.PedidoDTO;
import br.com.cwi.reset.tcc.dominio.dto.VisualizarPedidoDTO;
import br.com.cwi.reset.tcc.exceptions.FormaDePagamentoInvalidaException;
import br.com.cwi.reset.tcc.exceptions.ProdutoNaoPertenceAoEstabelecimentoException;
import br.com.cwi.reset.tcc.exceptions.QuantidadeMaximaDeProdutosExcedidaException;
import br.com.cwi.reset.tcc.exceptions.UsuarioSemEnderecoException;
import br.com.cwi.reset.tcc.repositories.PedidoRepository;
import br.com.cwi.reset.tcc.services.mappers.PedidoMapper;
import br.com.cwi.reset.tcc.utils.DataUtils;

@Service
public class PedidoService {

	private static final Integer QUANTIDADE_MAXIMA_DE_PRODUTOS = 5;

	@Autowired
	private PedidoRepository pedidoRepository;

	@Autowired
	private EstabelecimentoService estabelecimentoService;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private EnderecoService enderecoService;

	@Autowired
	private ProdutoService produtoService;

	public VisualizarPedidoDTO salvarProduto(@Valid PedidoDTO pedidoDto) {

		Estabelecimento estabelecimento = estabelecimentoService
				.buscarEstabelecimentoPorId(pedidoDto.getIdEstabelecimento());
		Usuario usuario = usuarioService.buscarUsuarioPorId(pedidoDto.getIdUsuarioSolicitante());
		Endereco endereco = enderecoService.buscarEnderecoPorUsuario(pedidoDto.getIdEnderecoEntrega(), usuario);

		List<Produto> produtos = new ArrayList<Produto>();
		// TODO TIRAR O MINUSHOUR() QUE FIZ PARA FUNCIONAR DEPOIS DAS 23 HRS
		LocalDateTime horaDoPedido = LocalDateTime.now().minusHours(7l);
		Integer tempoPreparo = 0;
		BigDecimal valorTotal = new BigDecimal(0.0);

		for (ItemPedidoDTO p : pedidoDto.getItens()) {
			Produto produto = produtoService.buscarProdutoPorId(p.getIdProduto());
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
		}
		LocalDateTime entrega = horaDoPedido.plusMinutes(tempoPreparo);
		validarPedido(estabelecimento, pedidoDto, usuario);

		Pedido pedido = PedidoMapper.mapearPedido(endereco, estabelecimento, pedidoDto, horaDoPedido, entrega, usuario,
				valorTotal);
		pedidoRepository.save(pedido);

		VisualizarPedidoDTO visualizar = PedidoMapper.mapearVisualizacaoPedido(pedido.getId(), endereco,
				pedido.getStatus(), tempoPreparo, valorTotal);

		return visualizar;
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
				// TODO TIRAR O MINUSHOUR() QUE FIZ PARA FUNCIONAR DEPOIS DAS 23 HRS
				if (DataUtils.horaAgora().minusHours(7l).isBefore(hr.getHorarioAbertura())
						|| DataUtils.horaAgora().minusHours(7l).isAfter(hr.getHorarioFechamento())) {
					throw new IllegalArgumentException("O estabelecimento está fechado");
				}
			}
			;
		});
	}

//	private List<Object> obterDados(PedidoDTO pedidoDto) {
//		List<Object> dados = new ArrayList<Object>();
//		Estabelecimento estabelecimento = estabelecimentoService
//				.buscarEstabelecimentoPorId(pedidoDto.getIdEstabelecimento());
//		Usuario usuario = usuarioService.buscarUsuarioPorId(pedidoDto.getIdUsuarioSolicitante());
//		Endereco endereco = enderecoService.buscarEnderecoPorUsuario(pedidoDto.getIdEnderecoEntrega(), usuario);
//		dados.add(estabelecimento);
//		dados.add(usuario);
//		dados.add(endereco);
//		return dados;
//	}

}
