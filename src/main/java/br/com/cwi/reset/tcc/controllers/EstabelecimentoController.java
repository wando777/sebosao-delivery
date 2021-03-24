package br.com.cwi.reset.tcc.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.cwi.reset.tcc.dominio.Endereco;
import br.com.cwi.reset.tcc.dominio.Estabelecimento;
import br.com.cwi.reset.tcc.services.EnderecoService;
import br.com.cwi.reset.tcc.services.EstabelecimentoService;

@RestController
@RequestMapping("/estabelecimentos")
public class EstabelecimentoController {

	@Autowired
	private EstabelecimentoService estabelecimentoService;
	
	@Autowired
	private EnderecoService enderecoService;

	@GetMapping
	public ResponseEntity<Page<Estabelecimento>> listar(
			@RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
			@RequestParam(value = "linhas", defaultValue = "10") Integer linhas) {
		Page<Estabelecimento> estabelecimentos = estabelecimentoService.paginarEstabelecimentos(pagina, linhas);
		return ResponseEntity.ok().body(estabelecimentos);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
		return ResponseEntity.ok().body(estabelecimentoService.buscarEstabelecimentoPorId(id));
	}

	@PostMapping
	public ResponseEntity<Estabelecimento> cadastrarEstabelecimento(
			@RequestBody @Valid Estabelecimento estabelecimento) {
		Estabelecimento estebelecimentoSalvo = estabelecimentoService.cadastrar(estabelecimento);
		return ResponseEntity.status(HttpStatus.CREATED).body(estebelecimentoSalvo);
	}
	
	@PostMapping("/{id}/enderecos")
	@ResponseStatus(HttpStatus.CREATED)
	public void adicionarEndereco(@PathVariable Long id, @RequestBody @Valid Endereco endereco) {
		enderecoService.salvarEnderecoPorEstabelecimento(id, endereco);
	}
	
	@DeleteMapping("/{id}/enderecos/{idEndereco}")
	public void removerEndereco(@PathVariable Long id, @PathVariable Long idEndereco) {
		enderecoService.removerEnderecoPorEstabelecimento(id, idEndereco);
	}

}
