package br.com.cwi.reset.tcc.services;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.com.cwi.reset.tcc.dominio.Estabelecimento;
import br.com.cwi.reset.tcc.dominio.Produto;
import br.com.cwi.reset.tcc.dominio.dto.ProdutoDTO;
import br.com.cwi.reset.tcc.exceptions.ObjetoNuloException;
import br.com.cwi.reset.tcc.repositories.ProdutoRepository;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private EstabelecimentoService estabelecimentoService;

	public Produto salvarProduto(@Valid ProdutoDTO produtodto) {
		Estabelecimento estabelecimento = estabelecimentoService
				.buscarEstabelecimentoPorId(produtodto.getIdEstabelecimento());
		Produto produto = new Produto();
		BeanUtils.copyProperties(produtodto, produto, "idEstabelecimento");
		produto.setEstabelecimento(estabelecimento);
		validarProduto(produto);
		return produtoRepository.save(produto);
	}

	public Produto buscarProdutoPorId(Long id) {
		Optional<Produto> produto = produtoRepository.findById(id);
		if (produto.isEmpty()) {
			throw new ObjetoNuloException("O produto não existe");
		}
		return produto.get();
	}

	public Page<Produto> paginarUsuarios(Integer pagina, Integer linhas) {
		PageRequest pageRequest = PageRequest.of(pagina, linhas, Direction.valueOf("ASC"), "descricao");
		return produtoRepository.findAll(pageRequest);
	}

	public void removerProduto(Long id) {
		produtoRepository.delete(buscarProdutoPorId(id));
	}

	private void validarProduto(Produto produto) {
		if (produto.getTempoPreparo() == null) {
			produto.setTempoPreparo(30);
		}
	}

}
