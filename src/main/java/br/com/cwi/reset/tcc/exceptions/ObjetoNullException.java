package br.com.cwi.reset.tcc.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ObjetoNullException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ObjetoNullException(String msg) {
		super(msg);
	}

}
