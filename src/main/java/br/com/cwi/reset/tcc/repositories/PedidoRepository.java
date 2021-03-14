package br.com.cwi.reset.tcc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.cwi.reset.tcc.dominio.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long>{

}
