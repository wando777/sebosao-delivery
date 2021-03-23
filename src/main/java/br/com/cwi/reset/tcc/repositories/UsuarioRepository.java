package br.com.cwi.reset.tcc.repositories;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.cwi.reset.tcc.dominio.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	boolean existsByEmail(String email);

	boolean existsByCpf(String cpf);

	@Transactional(readOnly = true)
	Usuario findByEmail(String email);

}
