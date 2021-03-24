package br.com.cwi.reset.tcc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.cwi.reset.tcc.dominio.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

}
