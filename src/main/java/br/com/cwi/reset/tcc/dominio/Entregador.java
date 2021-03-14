package br.com.cwi.reset.tcc.dominio;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.br.CPF;

@Entity
public class Entregador {

	public static final String REGEX_PLACA_VEICULO = ("([A-Z]{3}[ ])([0-9]{1}[A-Z]{1}[0-9]{2})|([A-Z]{3}[ ])([0-9]{4})");

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@CPF
	@NotBlank
	private String cpf;

	@NotBlank
	private String nome;

	@NotBlank
	private String telefone;

	@NotBlank
	@Pattern(regexp = REGEX_PLACA_VEICULO, message = "Não é possível cadastrar um entregador com uma placa de veículo fora do padrão: 'AAA 9999' ou 'AAA 9A99'")
	private String placaVeiculo;

	private Boolean disponivel = true;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getPlacaVeiculo() {
		return placaVeiculo;
	}

	public void setPlacaVeiculo(String placaVeiculo) {
		this.placaVeiculo = placaVeiculo;
	}

	public Boolean getDisponivel() {
		return disponivel;
	}

	public void setDisponivel(Boolean disponivel) {
		this.disponivel = disponivel;
	}
}
