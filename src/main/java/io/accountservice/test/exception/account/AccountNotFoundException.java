package io.accountservice.test.exception.account;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom AccountNotFound Exception with not found response
 * 
 * @author Austr0s
 */
@ResponseStatus(value =  HttpStatus.NOT_FOUND)
public class AccountNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 8140634038883014925L;

	public AccountNotFoundException(String message) {
		super(message);
	}
}