package br.com.cwi.reset.tcc.controllers;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.cwi.reset.tcc.dominio.Entregador;
import br.com.cwi.reset.tcc.services.EntregadorService;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/entregadores")
public class EntregadorController {

	@Autowired
	private EntregadorService entregadorService;

	@ApiOperation(value = "Lista os entregadores paginados.", notes = "Lista todos os entregadores de acordo com o número de linhas e página. Os elementos estão dispostos em ordem alfabética.")
	@GetMapping
	public ResponseEntity<Page<Entregador>> listar(@RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
			@RequestParam(value = "linhas", defaultValue = "10") Integer linhas) {
		Page<Entregador> entregadores = entregadorService.paginarEntregadores(pagina, linhas);
		return ResponseEntity.ok().body(entregadores);
	}

	@ApiOperation(value = "Cadastra um novo entregador.", notes = "Cadastra um novo entregador com as informações especificadas.")
	@PostMapping
	public ResponseEntity<Entregador> salvarEntregador(@RequestBody @Valid Entregador entregador,
			HttpServletResponse response) {
		Entregador entregadorSalvo = entregadorService.salvarEntregador(entregador);
		return ResponseEntity.status(HttpStatus.CREATED).body(entregadorSalvo);
	}

}
