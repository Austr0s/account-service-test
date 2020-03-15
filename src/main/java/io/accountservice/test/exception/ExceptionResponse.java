package io.accountservice.test.exception;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Exception Response entity to custom exceptions.
 * 
 * @author Austr0s
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponse {

	private Date timeStamp;
	
	private String message;
	
	private String details;

}
