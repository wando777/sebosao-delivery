package br.com.cwi.reset.tcc.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
import br.com.cwi.reset.tcc.dominio.StatusPedido;
import br.com.cwi.reset.tcc.dominio.Usuario;
import br.com.cwi.reset.tcc.dominio.dto.ItemPedidoDTO;
import br.com.cwi.reset.tcc.dominio.dto.PedidoDTO;
import br.com.cwi.reset.tcc.exceptions.ProdutoNaoPertenceAoEstabelecimentoException;
import br.com.cwi.reset.tcc.exceptions.QuantidadeMaximaDeProdutosExcedidaException;
import br.com.cwi.reset.tcc.repositories.PedidoRepository;
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

	public Pedido salvarProduto(@Valid PedidoDTO pedidoDto) {

		Estabelecimento estabelecimento = estabelecimentoService
				.buscarEstabelecimentoPorId(pedidoDto.getIdEstabelecimento());
		Usuario usuario = usuarioService.buscarUsuarioPorId(pedidoDto.getIdUsuarioSolicitante());
		Endereco endereco = enderecoService.buscarEnderecoPorUsuario(pedidoDto.getIdEnderecoEntrega(), usuario);

		List<Produto> produtos = new ArrayList<Produto>();
		LocalDateTime horaDoPedido = LocalDateTime.now();
		Integer tempoPreparo = 0;
		BigDecimal valorTotal = new BigDecimal(0.0);
		
		for (ItemPedidoDTO p : pedidoDto.getItens()) {
			Produto produto = produtoService.buscarProdutoPorId(p.getIdProduto());
			if (produto.getEstabelecimento().equals(estabelecimento)) {
				throw new ProdutoNaoPertenceAoEstabelecimentoException(
						"O Produto " + produto.getId() + "Não pertence ao estebelecimento " + estabelecimento.getId());
			}
			if (p.getQuantidade() > QUANTIDADE_MAXIMA_DE_PRODUTOS) {
				throw new QuantidadeMaximaDeProdutosExcedidaException(
						"A quantidade máxima de produtos é " + QUANTIDADE_MAXIMA_DE_PRODUTOS);
			}
			tempoPreparo += p.getQuantidade() * produto.getTempoPreparo();
			valorTotal.add(produto.getValor().multiply(new BigDecimal(p.getQuantidade())));
			produtos.add(produto);
		}
		LocalDateTime entrega = horaDoPedido.plusMinutes(tempoPreparo);
		validarPedido(estabelecimento, pedidoDto, usuario);

		Pedido pedido = new Pedido();
		pedido.setEnderecoEntrega(endereco);
		pedido.setEstabelecimento(estabelecimento);
		pedido.setFormaPagamento(pedidoDto.getFormaPagamento());
		pedido.setStatus(StatusPedido.EM_PREPARO);
		pedido.setHorarioSolicitacao(horaDoPedido);
		pedido.setHorarioSaiuParaEntrega(entrega);
		pedido.setSolicitante(usuario);
		pedido.setValorTotal(valorTotal);
		return pedidoRepository.save(pedido);
	}

	@SuppressWarnings("unlikely-arg-type")
	private void validarPedido(Estabelecimento estabelecimento, PedidoDTO pedidoDto, Usuario usuario) {
		FormaPagamento formaPagamento = pedidoDto.getFormaPagamento();
		if (!estabelecimento.getFormasPagamento().contains(formaPagamento)) {
			// TODO MODIFICAR O TIPO DA EXCEÇÃO
			throw new IllegalArgumentException("Esse estabelecimento não aceita essa forma de pagamento");
		}
		if (usuario.getEnderecos().isEmpty()) {
			throw new IllegalArgumentException("O usuário não possui endereços cadastrados");
		}
		estabelecimento.getHorariosFuncionamento().forEach(hr -> {
			if (hr.getDiaSemana().equals(DataUtils.diaDaSemanaHoje())) {
				if (DataUtils.horaAgora().isBefore(hr.getHorarioAbertura())
						|| DataUtils.horaAgora().isAfter(hr.getHorarioFechamento())) {
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
