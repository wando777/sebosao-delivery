package br.com.cwi.reset.tcc.dominio.dto;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import br.com.cwi.reset.tcc.dominio.Categoria;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoDTO {

	@NotBlank
	private String titulo;

	@NotBlank
	private String descricao;

	@DecimalMin(value = "0.0", message = "Valor mínimo deve ser igual a pelo menos R$ 0,00")
	@NotNull
	private BigDecimal valor;

	private String urlFoto;

	private Categoria categoria;

	private Integer tempoPreparo;

	@NotNull
	private Long idEstabelecimento;

	public ProdutoDTO(@NotBlank String titulo, @NotBlank String descricao,
			@DecimalMin(value = "0.0", message = "Valor mínimo deve ser igual a pelo menos R$ 0,00") @NotNull BigDecimal valor,
			String urlFoto, Categoria categoria, Integer tempoPreparo, Long idEstabelecimento) {
		this.titulo = titulo;
		this.descricao = descricao;
		this.valor = valor;
		this.urlFoto = urlFoto;
		this.categoria = categoria;
		this.tempoPreparo = tempoPreparo;
		this.idEstabelecimento = idEstabelecimento;
	}

}
