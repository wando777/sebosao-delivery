package br.com.cwi.reset.tcc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.cwi.reset.tcc.dominio.Entregador;

public interface EntregadorRepository extends JpaRepository<Entregador, Long>{

	boolean existsByCpf(String cpf);

	Entregador findFirstByDisponivel(boolean disponivel);

}
