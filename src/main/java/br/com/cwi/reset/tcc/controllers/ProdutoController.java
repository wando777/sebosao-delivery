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

import br.com.cwi.reset.tcc.dominio.Produto;
import br.com.cwi.reset.tcc.dominio.dto.ProdutoDTO;
import br.com.cwi.reset.tcc.services.ProdutoService;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

	@Autowired
	private ProdutoService produtoService;

	@PostMapping
	public ResponseEntity<Produto> salvarProduto(@RequestBody @Valid ProdutoDTO produtodto,
			HttpServletResponse response) {
		Produto produtoSalvo = produtoService.salvarProduto(produtodto);
		return ResponseEntity.status(HttpStatus.CREATED).body(produtoSalvo);
	}

}
