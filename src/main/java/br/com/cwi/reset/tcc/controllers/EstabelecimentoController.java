package br.com.cwi.reset.tcc.controllers;

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

import br.com.cwi.reset.tcc.dominio.Estabelecimento;
import br.com.cwi.reset.tcc.services.EstabelecimentoService;

@RestController
@RequestMapping("/estabelecimentos")
public class EstabelecimentoController {

	@Autowired
	private EstabelecimentoService estabelecimentoService;

	@GetMapping
	public ResponseEntity<Page<Estabelecimento>> listar(
			@RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
			@RequestParam(value = "linhas", defaultValue = "10") Integer linhas) {
		Page<Estabelecimento> estabelecimentos = estabelecimentoService.paginarEstabelecimentos(pagina, linhas);
		return ResponseEntity.ok().body(estabelecimentos);
	}

	@PostMapping
	public ResponseEntity<Estabelecimento> cadastrarEstabelecimento(
			@RequestBody @Valid Estabelecimento estabelecimento) {
		Estabelecimento estebelecimentoSalvo = estabelecimentoService.cadastrar(estabelecimento);
		return ResponseEntity.status(HttpStatus.CREATED).body(estebelecimentoSalvo);
	}

}
