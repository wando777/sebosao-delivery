package br.com.cwi.reset.tcc.controllers;

import javax.servlet.http.HttpServletResponse;
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
import org.springframework.web.bind.annotation.RestController;

import br.com.cwi.reset.tcc.dominio.Produto;
import br.com.cwi.reset.tcc.dominio.dto.ProdutoDTO;
import br.com.cwi.reset.tcc.services.ProdutoService;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

	@Autowired
	private ProdutoService produtoService;

	@GetMapping
	public ResponseEntity<Page<Produto>> listar(@RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
			@RequestParam(value = "linhas", defaultValue = "10") Integer linhas) {
		Page<Produto> produtos = produtoService.paginarUsuarios(pagina, linhas);
		return ResponseEntity.ok().body(produtos);
	}

	@PostMapping
	public ResponseEntity<Produto> salvarProduto(@RequestBody @Valid ProdutoDTO produtoDto,
			HttpServletResponse response) {
		Produto produtoSalvo = produtoService.salvarProduto(produtoDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(produtoSalvo);
	}
	
	@DeleteMapping("/{id}")
	public void removerProduto(@PathVariable Long id) {
		produtoService.removerProduto(id);
	}

}
