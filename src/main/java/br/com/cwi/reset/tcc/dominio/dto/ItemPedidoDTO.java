package br.com.cwi.reset.tcc.dominio.dto;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemPedidoDTO {

	@NotNull
	private Long idProduto;

	@NotNull
	private Integer quantidade;

}
