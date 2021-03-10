package br.com.cwi.reset.tcc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cwi.reset.tcc.dominio.Endereco;
import br.com.cwi.reset.tcc.dominio.Usuario;
import br.com.cwi.reset.tcc.exceptions.objetoNuloException;
import br.com.cwi.reset.tcc.repositories.EnderecoRepository;
import br.com.cwi.reset.tcc.repositories.UsuarioRepository;

@Service
public class EnderecoService {

	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private UsuarioService usuarioService;

	public Endereco salvarEndereco(Endereco endereco) {
		endereco.setId(null);
		return enderecoRepository.save(endereco);
	}

	public void salvarEnderecoPorUsuario(Long id, Endereco endereco) {
		Usuario user = usuarioService.buscarUsuarioPorId(id);
		user.getEnderecos().add(endereco);
		usuarioRepository.save(user);
	}

	public Endereco buscarEnderecoPorId(Long id) {
		Optional<Endereco> end = enderecoRepository.findById(id);
		if (end.isEmpty()) {
			throw new objetoNuloException("Este endereço não foi cadastrado");
		}
		return end.get();
	}

	public void removerEnderecoPorUsuario(Long id, Long idEndereco) {
		Usuario user = usuarioService.buscarUsuarioPorId(id);
		Endereco end = buscarEnderecoPorId(idEndereco);
		if (!validaEnderecoDoUsuario(end, user)) {
			throw new objetoNuloException("Não foi encontrado o endereço de id: " + idEndereco + " para o usuario.");
		}
		user.getEnderecos().remove(end);
		usuarioRepository.save(user);
		enderecoRepository.deleteById(idEndereco);
	}

	public boolean validaEnderecoDoUsuario(Endereco endereco, Usuario usuario) {
//		List<Endereco> enderecos = usuario.getEnderecos().stream().filter(end -> end.equals(endereco))
//				.collect(Collectors.toList());
		return usuario.getEnderecos().contains(endereco);
	}

}
