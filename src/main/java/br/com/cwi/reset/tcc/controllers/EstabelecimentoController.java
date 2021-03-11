package br.com.cwi.reset.tcc.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cwi.reset.tcc.dominio.Estabelecimento;
import br.com.cwi.reset.tcc.repositories.EstabelecimentoRepository;
import br.com.cwi.reset.tcc.services.EstabelecimentoService;

@RestController
@RequestMapping("/estabelecimentos")
public class EstabelecimentoController {

	@Autowired
	private EstabelecimentoService estabelecimentoService;

	@Autowired
	private EstabelecimentoRepository teste;

	@GetMapping
	public List<Estabelecimento> listar() {
		//TODO Fazer paginação
		return teste.findAll();
	}

	@PostMapping
	public ResponseEntity<Estabelecimento> cadastrarEstabelecimento(
			@RequestBody @Valid Estabelecimento estabelecimento) {
		Estabelecimento estebelecimentoSalvo = estabelecimentoService.cadastrar(estabelecimento);
		return ResponseEntity.status(HttpStatus.CREATED).body(estebelecimentoSalvo);
	}

}
