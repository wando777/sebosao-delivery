package br.com.cwi.reset.tcc.services.mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import br.com.cwi.reset.tcc.dominio.Endereco;
import br.com.cwi.reset.tcc.dominio.Entregador;
import br.com.cwi.reset.tcc.dominio.Estabelecimento;
import br.com.cwi.reset.tcc.dominio.ItemPedido;
import br.com.cwi.reset.tcc.dominio.Pedido;
import br.com.cwi.reset.tcc.dominio.StatusPedido;
import br.com.cwi.reset.tcc.dominio.Usuario;
import br.com.cwi.reset.tcc.dominio.dto.ConsultarItemPedidoDTO;
import br.com.cwi.reset.tcc.dominio.dto.ConsultarPedidoDTO;
import br.com.cwi.reset.tcc.dominio.dto.PedidoDTO;
import br.com.cwi.reset.tcc.dominio.dto.VisualizarPedidoDTO;

public class PedidoMapper {

	public static VisualizarPedidoDTO mapearVisualizacaoPedido(Long idPedido, Endereco enderecoEntrega,
			StatusPedido statusPedido, Integer tempoEstimadoEmMinuto, BigDecimal valorTotal) {
		VisualizarPedidoDTO visualizar = new VisualizarPedidoDTO();
		visualizar.setEnderecoEntrega(enderecoEntrega);
		visualizar.setIdPedido(idPedido);
		visualizar.setStatusPedido(statusPedido);
		visualizar.setTempoEstimadoEmMinuto(tempoEstimadoEmMinuto);
		visualizar.setValorTotal(valorTotal);
		return visualizar;
	}

	public static Pedido mapearPedido(Endereco endereco, Estabelecimento estabelecimento, PedidoDTO pedidoDto,
			LocalDateTime horaDoPedido, LocalDateTime entrega, Usuario usuario, BigDecimal valorTotal,
			List<ItemPedido> itensPedido) {
		Pedido pedido = new Pedido();
		pedido.setEnderecoEntrega(endereco);
		pedido.setEstabelecimento(estabelecimento);
		pedido.setFormaPagamento(pedidoDto.getFormaPagamento());
		pedido.setStatus(StatusPedido.EM_PREPARO);
		pedido.setHorarioSolicitacao(horaDoPedido);
		pedido.setHorarioEntrega(entrega);
		pedido.setSolicitante(usuario);
		pedido.setValorTotal(valorTotal);
		pedido.setItensPedido(itensPedido);
		return pedido;
	}

	public static ConsultarPedidoDTO mapearConsultaPedido(String nomeSolicitante, Endereco enderecoEntrega,
			String nomeEstabelecimento, BigDecimal valorTotal, Entregador entregador,
			LocalDateTime horarioPrevistoParaEntrega, StatusPedido situacao, List<ConsultarItemPedidoDTO> itensPedido) {
		ConsultarPedidoDTO consultar = new ConsultarPedidoDTO();
		consultar.setNomeSolicitante(nomeSolicitante);
		consultar.setEnderecoEntrega(enderecoEntrega);
		consultar.setNomeEstabelecimento(nomeEstabelecimento);
		consultar.setValorTotal(valorTotal);
		consultar.setEntregador(entregador);
		consultar.setHorarioPrevistoParaEntrega(horarioPrevistoParaEntrega);
		consultar.setSituacao(situacao);
		consultar.setItensPedido(itensPedido);
		return consultar;
	}

	public static Pedido mapearCancelarPedido(Pedido pedido) {
		pedido.setStatus(StatusPedido.CANCELADO);
		pedido.setHorarioCancelamento(LocalDateTime.now());
		return pedido;
	}

	public static Pedido mapearFinalizarPedido(Pedido pedido) {
		pedido.setStatus(StatusPedido.ENTREGUE);
		pedido.setHorarioEntrega(LocalDateTime.now());
		pedido.getEntregador().setDisponivel(true);
		return pedido;
	}

	public static Pedido mapearEntregarPedido(Pedido pedido, Entregador entregador) {
		pedido.setEntregador(entregador);
		pedido.setHorarioSaiuParaEntrega(LocalDateTime.now());
		pedido.setStatus(StatusPedido.SAIU_PARA_ENTREGA);
		pedido.getEntregador().setDisponivel(false);
		return pedido;
	}

}
