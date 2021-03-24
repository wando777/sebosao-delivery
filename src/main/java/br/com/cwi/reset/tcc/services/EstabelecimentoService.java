package br.com.cwi.reset.tcc.services;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.com.cwi.reset.tcc.dominio.Estabelecimento;
import br.com.cwi.reset.tcc.dominio.HorarioFuncionamento;
import br.com.cwi.reset.tcc.exceptions.EntidadeJaCadastradaException;
import br.com.cwi.reset.tcc.exceptions.ObjetoNuloException;
import br.com.cwi.reset.tcc.repositories.EstabelecimentoRepository;

@Service
public class EstabelecimentoService {

	@Autowired
	private EstabelecimentoRepository estabelecimentoRepository;

	public Estabelecimento cadastrar(@Valid Estabelecimento estabelecimento) {
		estabelecimento.setId(null);
		validaEstabelecimento(estabelecimento);
		return estabelecimentoRepository.save(estabelecimento);
	}

	public Page<Estabelecimento> paginarEstabelecimentos(Integer pagina, Integer linhas) {
		PageRequest pageRequest = PageRequest.of(pagina, linhas, Direction.valueOf("ASC"), "nomeFantasia");
		return estabelecimentoRepository.findAll(pageRequest);
	}

	public Estabelecimento buscarEstabelecimentoPorId(Long id) {
		Optional<Estabelecimento> estabelecimento = estabelecimentoRepository.findById(id);
		if (estabelecimento.isEmpty()) {
			throw new ObjetoNuloException("O estabelecimento não existe");
		}
		return estabelecimento.get();
	}

	private boolean isVazio(HorarioFuncionamento horario) {
		boolean retorno = false;
		if (horario.getHorarioAbertura() == null) {
			retorno = true;
		}
		if (horario.getHorarioFechamento() == null) {
			retorno = true;
		}
		return retorno;
	}

	private void validaEstabelecimento(Estabelecimento estabelecimento) {
		if (estabelecimentoRepository.existsByCnpj(estabelecimento.getCnpj())) {
			throw new EntidadeJaCadastradaException("Esse CNPJ " + estabelecimento.getCnpj() + " já foi cadastrado.");
		}
		estabelecimento.getHorariosFuncionamento().forEach(horario -> {
			if (isVazio(horario)) {
				throw new ObjetoNuloException("É preciso definir um horário de funcionamento");
			}
			;
		});

	}

}
