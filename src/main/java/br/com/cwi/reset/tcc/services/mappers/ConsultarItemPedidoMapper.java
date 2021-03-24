package br.com.cwi.reset.tcc.services.mappers;

import br.com.cwi.reset.tcc.dominio.ItemPedido;
import br.com.cwi.reset.tcc.dominio.dto.ConsultarItemPedidoDTO;

public class ConsultarItemPedidoMapper {

	public static ConsultarItemPedidoDTO mapearConsultarItemPedido(ItemPedido item, ConsultarItemPedidoDTO itenSalvo) {
		itenSalvo.setTitulo(item.getProduto().getTitulo());
		itenSalvo.setQuantidade(item.getQuantidade());
		return itenSalvo;
	}

}
