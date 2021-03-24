package br.com.cwi.reset.tcc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.cwi.reset.tcc.dominio.Usuario;
import br.com.cwi.reset.tcc.repositories.UsuarioRepository;
import br.com.cwi.reset.tcc.security.UserSS;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.findByEmail(username);
		if (usuario == null) {
			throw new UsernameNotFoundException(username);
		}
		return new UserSS(usuario.getId(), usuario.getEmail(), usuario.getSenha());
	}

}
