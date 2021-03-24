package br.com.cwi.reset.tcc.services;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cwi.reset.tcc.dominio.Endereco;
import br.com.cwi.reset.tcc.dominio.Estabelecimento;
import br.com.cwi.reset.tcc.dominio.Usuario;
import br.com.cwi.reset.tcc.exceptions.ObjetoNuloException;
import br.com.cwi.reset.tcc.repositories.EnderecoRepository;
import br.com.cwi.reset.tcc.repositories.EstabelecimentoRepository;
import br.com.cwi.reset.tcc.repositories.UsuarioRepository;

@Service
public class EnderecoService {

	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private EstabelecimentoRepository estabelecimentoRepository;

	@Autowired
	private EstabelecimentoService estabelecimentoService;

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
			throw new ObjetoNuloException("Este endereço não foi cadastrado");
		}
		return end.get();
	}

	public Endereco buscarEnderecoPorUsuario(Long idEndereco, Usuario usuario) {
		Endereco end = buscarEnderecoPorId(idEndereco);
		if (!validaEnderecoDoUsuario(end, usuario)) {
			throw new ObjetoNuloException("Não foi encontrado o endereço de id: " + idEndereco + " para o usuario.");
		}
		return end;
	}

	public void removerEnderecoPorUsuario(Long id, Long idEndereco) {
		Usuario user = usuarioService.buscarUsuarioPorId(id);
		Endereco end = buscarEnderecoPorUsuario(idEndereco, user);
		user.getEnderecos().remove(end);
		usuarioRepository.save(user);
		enderecoRepository.deleteById(idEndereco);
	}

	public void salvarEnderecoPorEstabelecimento(Long id, @Valid Endereco endereco) {
		Estabelecimento estabelecimento = estabelecimentoService.buscarEstabelecimentoPorId(id);
		estabelecimento.getEnderecos().add(endereco);
		estabelecimentoRepository.save(estabelecimento);
	}

	public void removerEnderecoPorEstabelecimento(Long id, Long idEndereco) {
		Estabelecimento estabelecimento = estabelecimentoService.buscarEstabelecimentoPorId(id);
		Endereco end = buscarEnderecoPorId(idEndereco);
		if (!estabelecimento.getEnderecos().contains(end)) {
			throw new ObjetoNuloException(
					"Não foi encontrado o endereço de id: " + idEndereco + " para o estabelecimento.");
		}
		estabelecimento.getEnderecos().remove(end);
		estabelecimentoRepository.save(estabelecimento);
		enderecoRepository.deleteById(idEndereco);
	}

	public boolean validaEnderecoDoUsuario(Endereco endereco, Usuario usuario) {
//		List<Endereco> enderecos = usuario.getEnderecos().stream().filter(end -> end.equals(endereco))
//				.collect(Collectors.toList());
		return usuario.getEnderecos().contains(endereco);
	}

}
