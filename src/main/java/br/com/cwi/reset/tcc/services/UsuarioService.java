package br.com.cwi.reset.tcc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.cwi.reset.tcc.dominio.Usuario;
import br.com.cwi.reset.tcc.dominio.dto.UsuarioDTO;
import br.com.cwi.reset.tcc.exceptions.EntidadeJaCadastradaException;
import br.com.cwi.reset.tcc.exceptions.ObjetoNuloException;
import br.com.cwi.reset.tcc.repositories.UsuarioRepository;
import br.com.cwi.reset.tcc.services.mappers.UsuarioMapper;

@Service
public class UsuarioService {

	@Autowired
	private BCryptPasswordEncoder pass;

	@Autowired
	private UsuarioRepository usuarioRepository;

	public Usuario salvarUsuario(Usuario user) {
		user.setId(null);
		validaUsuario(user);
		user.setSenha(pass.encode(user.getSenha()));
		return usuarioRepository.save(user);
	}

	public Page<Usuario> paginarUsuarios(Integer pagina, Integer linhas) {
		PageRequest pageRequest = PageRequest.of(pagina, linhas, Direction.valueOf("ASC"), "nome");
		return usuarioRepository.findAll(pageRequest);
	}

	public Usuario buscarUsuarioPorId(Long id) {
		Optional<Usuario> user = usuarioRepository.findById(id);
		if (user.isEmpty()) {
			throw new ObjetoNuloException("O usuário não existe");
		}
		return user.get();
	}

	public Usuario atualizar(Long id, UsuarioDTO usuarioDto) {
		validaEmail(usuarioDto.getEmail());
		Usuario usuarioNovo = buscarUsuarioPorId(id);
		// BeanUtils.copyProperties(usuarioDto, usuarioNovo, "id", "cpf");
		usuarioNovo = UsuarioMapper.usuarioMapper(usuarioDto);
		return usuarioRepository.save(usuarioNovo);
	}

	private void validaUsuario(Usuario user) {
		validaEmail(user.getEmail());
		if (usuarioRepository.existsByCpf(user.getCpf())) {
			throw new EntidadeJaCadastradaException("Esse CPF " + user.getCpf() + " já foi cadastrado.");
		}
	}

	private void validaEmail(String email) {
		if (usuarioRepository.existsByEmail(email)) {
			throw new EntidadeJaCadastradaException("Esse e-mail " + email + " já foi cadastrado.");
		}
	}

}
