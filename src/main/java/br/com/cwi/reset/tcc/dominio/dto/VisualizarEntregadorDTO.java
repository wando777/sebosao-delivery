package br.com.cwi.reset.tcc.dominio.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VisualizarEntregadorDTO {

	private Long id;
	private String nome;
	private String cpf;
	private String telefone;
	private String placaVeiculo;

}
