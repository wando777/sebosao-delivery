package br.com.cwi.reset.tcc.services.mappers;

import br.com.cwi.reset.tcc.dominio.Entregador;
import br.com.cwi.reset.tcc.dominio.dto.VisualizarEntregadorDTO;

public class EntregadorMapper {

	public static VisualizarEntregadorDTO mapearVisualizarEntregador(Entregador entregador) {
		VisualizarEntregadorDTO visualizarEntregador = new VisualizarEntregadorDTO();
		visualizarEntregador.setNome(entregador.getNome());
		visualizarEntregador.setId(entregador.getId());
		visualizarEntregador.setCpf(entregador.getCpf());
		visualizarEntregador.setPlacaVeiculo(entregador.getPlacaVeiculo());
		visualizarEntregador.setTelefone(entregador.getTelefone());
		return visualizarEntregador;
	}

}
