package br.com.cwi.reset.tcc.controllers;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
	public ResponseEntity<VisualizarPedidoDTO> salvarProduto(@RequestBody @Valid PedidoDTO pedidoDto,
			HttpServletResponse response) {
		VisualizarPedidoDTO pedidoSalvo = pedidoService.salvarProduto(pedidoDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(pedidoSalvo);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
		return ResponseEntity.ok().body(pedidoService.buscarPedidoPorId(id));
	}

	@DeleteMapping("/{id}")
	public void cancelarPedido(@PathVariable Long id) {
		pedidoService.cancelarPedido(id);
	}
	
	@PutMapping("/{id}/entregar")
	public ResponseEntity<?> entregarPedido(@PathVariable Long id) {
		return ResponseEntity.ok(pedidoService.entregarPedido(id));
	}
	
	@PutMapping("/{id}/finalizar")
	public ResponseEntity<?> finalizarPedido(@PathVariable Long id) {
		return ResponseEntity.ok(pedidoService.finalizarPedido(id));
	}
}
