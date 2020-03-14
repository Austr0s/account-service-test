package io.accountservice.test.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import io.accountservice.test.exception.account.AccountNotFoundException;

/**
 * Custom ApiRest exception handler. Here we declare all custom exception handlers.
 * 
 * @author Austr0s
 *
 */
@ControllerAdvice
@RestController
public class ApiRestExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ExceptionResponse> exceptionCustomHandler(Exception e, WebRequest request) {
		ExceptionResponse error = new ExceptionResponse(new Date(), e.getMessage(),
				request.getDescription(false));
		return new ResponseEntity<ExceptionResponse>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionResponse> exceptionHandler(Exception e, WebRequest request) {
		ExceptionResponse error = new ExceptionResponse(new Date(), e.getMessage(),
		        request.getDescription(false));
		return new ResponseEntity<ExceptionResponse>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(AccountNotFoundException.class)
	public final ResponseEntity<ExceptionResponse> handleAccountNotFoundException(AccountNotFoundException e,
			WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), e.getMessage(),
				request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
	}
	

}
