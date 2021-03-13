package br.com.cwi.reset.tcc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.cwi.reset.tcc.dominio.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

}
