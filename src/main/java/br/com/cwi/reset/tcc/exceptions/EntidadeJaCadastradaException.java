package br.com.cwi.reset.tcc.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EntidadeJaCadastradaException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EntidadeJaCadastradaException(String msg) {
		super(msg);
	}

}
