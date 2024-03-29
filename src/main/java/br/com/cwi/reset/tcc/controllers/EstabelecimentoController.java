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
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/estabelecimentos")
public class EstabelecimentoController {

	@Autowired
	private EstabelecimentoService estabelecimentoService;

	@Autowired
	private EnderecoService enderecoService;

	@ApiOperation(value = "Lista os estabelecimentos paginados.", notes = "Lista todos os estabelecimentos de acordo com o número de linhas e página. Os elementos estão dispostos em ordem alfabética.")
	@GetMapping
	public ResponseEntity<Page<Estabelecimento>> listar(
			@RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
			@RequestParam(value = "linhas", defaultValue = "10") Integer linhas) {
		Page<Estabelecimento> estabelecimentos = estabelecimentoService.paginarEstabelecimentos(pagina, linhas);
		return ResponseEntity.ok().body(estabelecimentos);
	}

	@ApiOperation(value = "Busca estebelecimento por ID.", notes = "Busca estabelecimento de acordo com o ID especificado.")
	@GetMapping("/{id}")
	public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
		return ResponseEntity.ok().body(estabelecimentoService.buscarEstabelecimentoPorId(id));
	}

	@ApiOperation(value = "Cadastra um novo estabelecimento.", notes = "Cadastra um novo estebelecimento com as informações especificadas.")
	@PostMapping
	public ResponseEntity<Estabelecimento> cadastrarEstabelecimento(
			@RequestBody @Valid Estabelecimento estabelecimento) {
		Estabelecimento estebelecimentoSalvo = estabelecimentoService.cadastrar(estabelecimento);
		return ResponseEntity.status(HttpStatus.CREATED).body(estebelecimentoSalvo);
	}

	@ApiOperation(value = "Cadastra um novo endereço ao estabelecimento ID.", notes = "Cadastra um novo endereço com as informações especificadas para um estabelecimento.")
	@PostMapping("/{id}/enderecos")
	@ResponseStatus(HttpStatus.CREATED)
	public void adicionarEndereco(@PathVariable Long id, @RequestBody @Valid Endereco endereco) {
		enderecoService.salvarEnderecoPorEstabelecimento(id, endereco);
	}

	@ApiOperation(value = "Remove o endereço de um estabelecimento.", notes = "Busca estabelecimento de acordo com o ID especificado e remove o endereço também especificado.")
	@DeleteMapping("/{id}/enderecos/{idEndereco}")
	public void removerEndereco(@PathVariable Long id, @PathVariable Long idEndereco) {
		enderecoService.removerEnderecoPorEstabelecimento(id, idEndereco);
	}

}
