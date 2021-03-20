package br.com.cwi.reset.tcc.dominio.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConsultarItemPedidoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String titulo;
	private Integer quantidade;
}
