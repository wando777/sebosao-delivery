package br.com.cwi.reset.tcc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.cwi.reset.tcc.dominio.Estabelecimento;

public interface EstabelecimentoRepository extends JpaRepository<Estabelecimento, Long>{

	boolean existsByCnpj(String cnpj);

}
