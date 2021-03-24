package br.com.cwi.reset.tcc.services;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.com.cwi.reset.tcc.dominio.Entregador;
import br.com.cwi.reset.tcc.exceptions.EntidadeJaCadastradaException;
import br.com.cwi.reset.tcc.exceptions.ObjetoNuloException;
import br.com.cwi.reset.tcc.repositories.EntregadorRepository;

@Service
public class EntregadorService {

	@Autowired
	private EntregadorRepository entregadorRepository;

	public Entregador salvarEntregador(@Valid Entregador entregador) {
		entregador.setId(null);
		validarEntregador(entregador);
		return entregadorRepository.save(entregador);
	}

	private void validarEntregador(@Valid Entregador entregador) {
		if (entregadorRepository.existsByCpf(entregador.getCpf())) {
			throw new EntidadeJaCadastradaException("Esse CPF " + entregador.getCpf() + " já foi cadastrado.");
		}
	}

	public Page<Entregador> paginarEntregadores(Integer pagina, Integer linhas) {
		PageRequest pageRequest = PageRequest.of(pagina, linhas, Direction.valueOf("ASC"), "nome");
		return entregadorRepository.findAll(pageRequest);
	}

	public Entregador getEntregadorDisponivel() {
		boolean disponivel = true;
		Entregador entregador = entregadorRepository.findFirstByDisponivel(disponivel);
		if (entregador == null) {
			throw new ObjetoNuloException("Não há entregador disponível no momento, tente novamente mais tarde.");
		}
		return entregador;
	}

}
