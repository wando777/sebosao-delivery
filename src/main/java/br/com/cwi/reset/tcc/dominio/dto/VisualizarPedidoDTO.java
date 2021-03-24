package br.com.cwi.reset.tcc.dominio.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.cwi.reset.tcc.dominio.Endereco;
import br.com.cwi.reset.tcc.dominio.StatusPedido;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VisualizarPedidoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idPedido;
	private Integer tempoEstimadoEmMinuto;
	private BigDecimal valorTotal;
	private StatusPedido statusPedido;
	private Endereco enderecoEntrega;
}
