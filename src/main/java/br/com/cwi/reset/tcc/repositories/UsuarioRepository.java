package br.com.cwi.reset.tcc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.cwi.reset.tcc.dominio.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	boolean existsByEmail(String email);

	boolean existsByCpf(String cpf);

}
