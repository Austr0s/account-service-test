package io.accountservice.test.exception;

import java.beans.ConstructorProperties;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Custom Exception for standard exception with custom message.
 * 
 * @author Austr0s
 */
@Getter
@Service
@NoArgsConstructor
@ResponseStatus(value =  HttpStatus.BAD_REQUEST)
public class CustomException extends RuntimeException {

	private static final long serialVersionUID = 3444922657550165341L;

	private String errorMessage;
	

	@ConstructorProperties({ "errorMessage" })
	public CustomException(String errorMessage) {
		super(errorMessage);
		this.errorMessage = errorMessage;
	}

}
