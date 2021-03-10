package br.com.cwi.reset.tcc.services;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.com.cwi.reset.tcc.dominio.Usuario;
import br.com.cwi.reset.tcc.exceptions.mesmoEmailException;
import br.com.cwi.reset.tcc.exceptions.objetoNuloException;
import br.com.cwi.reset.tcc.repositories.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	public Usuario salvarUsuario(Usuario user) {
		user.setId(null);
		validaUsuario(user);

		return usuarioRepository.save(user);
	}

	private void validaUsuario(Usuario user) {
		if (usuarioRepository.existsByEmail(user.getEmail())) {
			throw new mesmoEmailException("Esse e-mail " + user.getEmail() + " já foi cadastrado.");
		}
		if (usuarioRepository.existsByCpf(user.getCpf())) {
			throw new mesmoEmailException("Esse CPF " + user.getCpf() + " já foi cadastrado.");
		}
	}

	public Page<Usuario> paginarUsuarios(Integer pagina, Integer linhas) {
		PageRequest pageRequest = PageRequest.of(pagina, linhas, Direction.valueOf("ASC"), "nome");
		return usuarioRepository.findAll(pageRequest);
	}

	public Usuario buscarUsuarioPorId(Long id) {
		Optional<Usuario> user = usuarioRepository.findById(id);
		if (user.isEmpty()) {
			throw new objetoNuloException("O usuário não existe");
		}
		return user.get();
	}

	public Usuario atualizar(Long id, Usuario usuario) {
		if (usuarioRepository.existsByEmail(usuario.getEmail())) {
			throw new mesmoEmailException("Esse e-mail " + usuario.getEmail() + " já foi cadastrado.");
		}
		Usuario usuarioNovo = buscarUsuarioPorId(id);
		BeanUtils.copyProperties(usuario, usuarioNovo, "id", "cpf");
		//TODO Remover o antigo endereço cadastrado
		return usuarioRepository.save(usuarioNovo);
	}

}
