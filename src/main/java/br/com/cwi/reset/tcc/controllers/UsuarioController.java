package br.com.cwi.reset.tcc.controllers;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.cwi.reset.tcc.dominio.Endereco;
import br.com.cwi.reset.tcc.dominio.Usuario;
import br.com.cwi.reset.tcc.dominio.dto.UsuarioDTO;
import br.com.cwi.reset.tcc.services.EnderecoService;
import br.com.cwi.reset.tcc.services.UsuarioService;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private EnderecoService enderecoService;

	@ApiOperation(value = "Lista os usuários paginados.", notes = "Lista todos os usuários de acordo com o número de linhas e página. Os elementos estão dispostos em ordem alfabética.")
	@GetMapping
	public ResponseEntity<Page<Usuario>> listar(@RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
			@RequestParam(value = "linhas", defaultValue = "10") Integer linhas) {
		Page<Usuario> usuarios = usuarioService.paginarUsuarios(pagina, linhas);
		return ResponseEntity.ok().body(usuarios);
	}
	
	@ApiOperation(value = "Busca usuário por ID.", notes = "Busca usuário de acordo com o ID especificado.")
	@GetMapping("/{id}")
	public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
		return ResponseEntity.ok().body(usuarioService.buscarUsuarioPorId(id));
	}

	@ApiOperation(value = "Cria um novo usuário.", notes = "Cria um novo usuário com as informações especificadas. Este usuário é necessário para logar no sistema e ter acesso a todas as requisições.")
	@PostMapping
	public ResponseEntity<Usuario> saveUser(@RequestBody @Valid Usuario user, HttpServletResponse response) {
		Usuario usuarioSalvo = usuarioService.salvarUsuario(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo);
	}

	@PostMapping("/{id}/enderecos")
	@ResponseStatus(HttpStatus.CREATED)
	public void adicionarEndereco(@PathVariable Long id, @RequestBody @Valid Endereco endereco) {
		enderecoService.salvarEnderecoPorUsuario(id, endereco);
	}

	@ApiOperation(value = "Remove o endereço de um usuário.", notes = "Busca usuário de acordo com o ID especificado e remove o endereço também especificado.")
	@DeleteMapping("/{id}/enderecos/{idEndereco}")
	public void removerEndereco(@PathVariable Long id, @PathVariable Long idEndereco) {
		enderecoService.removerEnderecoPorUsuario(id, idEndereco);
	}

	@ApiOperation(value = "Atualiza usuário.", notes = "Atualiza cadastro do usuário.")
	@PutMapping("/{id}")
	public ResponseEntity<Usuario> atualizar(@PathVariable Long id, @RequestBody @Valid UsuarioDTO usuarioDto) {
		Usuario usuarioNovo = usuarioService.atualizar(id, usuarioDto);
		return ResponseEntity.ok(usuarioNovo);
	}
}
