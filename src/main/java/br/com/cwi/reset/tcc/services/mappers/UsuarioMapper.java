package br.com.cwi.reset.tcc.services.mappers;

import br.com.cwi.reset.tcc.dominio.Usuario;
import br.com.cwi.reset.tcc.dominio.dto.UsuarioDTO;

public class UsuarioMapper {

	public static Usuario usuarioMapper(UsuarioDTO usuarioDto) {
		Usuario usuario = new Usuario();
		usuario.setEmail(usuarioDto.getEmail());
		usuario.setNome(usuarioDto.getNome());
		usuario.setSenha(usuarioDto.getSenha());
		usuario.setEnderecos(usuarioDto.getEnderecos());
		return usuario;
	}

}
