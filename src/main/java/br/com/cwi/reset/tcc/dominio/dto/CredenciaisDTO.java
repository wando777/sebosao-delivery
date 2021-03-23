package br.com.cwi.reset.tcc.dominio.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CredenciaisDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userName;
	private String senha;

}
