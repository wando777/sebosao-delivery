package br.com.cwi.reset.tcc.services;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cwi.reset.tcc.dominio.Estabelecimento;
import br.com.cwi.reset.tcc.dominio.Produto;
import br.com.cwi.reset.tcc.dominio.dto.ProdutoDTO;
import br.com.cwi.reset.tcc.repositories.ProdutoRepository;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private EstabelecimentoService estabelecimentoService;

	public Produto salvarProduto(@Valid Produto produto) {
		produto.setId(null);
		validarProduto(produto);
		// Estabelecimento estabelecimento =
		// estabelecimentoService.buscarEstabelecimentoPorId();

		return produtoRepository.save(produto);
	}

	public Produto salvarProduto(@Valid ProdutoDTO produtodto) {
		Estabelecimento estabelecimento = estabelecimentoService
				.buscarEstabelecimentoPorId(produtodto.getIdEstabelecimento());
		Produto produto = new Produto();
		BeanUtils.copyProperties(produtodto, produto, "idEstabelecimento");
		produto.setEstabelecimento(estabelecimento);
		return produtoRepository.save(produto);
	}

	private void validarProduto(@Valid Produto produto) {
		// TODO Auto-generated method stub

	}

	public Produto buscarProdutoPorId(Long id) {
		return produtoRepository.findById(id).get();
	}

}
