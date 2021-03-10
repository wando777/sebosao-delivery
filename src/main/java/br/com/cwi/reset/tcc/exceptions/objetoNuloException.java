package br.com.cwi.reset.tcc.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class objetoNuloException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public objetoNuloException(String msg) {
		super(msg);
	}

}
