package br.com.cwi.reset.tcc.dominio.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import br.com.cwi.reset.tcc.dominio.Endereco;
import br.com.cwi.reset.tcc.dominio.Entregador;
import br.com.cwi.reset.tcc.dominio.StatusPedido;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConsultarPedidoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nomeSolicitante;
	private Endereco enderecoEntrega;
	private String nomeEstabelecimento;
	private List<ConsultarItemPedido> itensPedido;
	private BigDecimal valorTotal;
	private Entregador entregador;
	private LocalDateTime horarioPrevistoParaEntrega;
	private StatusPedido situacao;
}
