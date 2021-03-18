package br.com.cwi.reset.tcc.controllers;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cwi.reset.tcc.dominio.dto.PedidoDTO;
import br.com.cwi.reset.tcc.dominio.dto.VisualizarPedidoDTO;
import br.com.cwi.reset.tcc.services.PedidoService;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

	@Autowired
	private PedidoService pedidoService;

	@PostMapping
	public ResponseEntity<VisualizarPedidoDTO> salvarProduto(@RequestBody @Valid PedidoDTO pedidoDto, HttpServletResponse response) {
		VisualizarPedidoDTO pedidoSalvo = pedidoService.salvarProduto(pedidoDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(pedidoSalvo);
	}

}
