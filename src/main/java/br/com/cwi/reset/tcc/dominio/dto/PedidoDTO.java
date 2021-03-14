package br.com.cwi.reset.tcc.dominio.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PedidoDTO {

	@NotNull
	private Long idEstabelecimento;

	@NotNull
	private Long idUsuarioSolicitante;

	@NotNull
	private Long idEnderecoEntrega;

	@NotBlank
	private String formaPagamento;

	@NotNull
	private List<ItemPedidoDTO> itens;
}
