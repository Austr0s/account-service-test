package io.accountservice.test.model.dto;

import java.io.Serializable;

import io.accountservice.test.model.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO to get data on requests to transactions.
 * 
 * @author Austr0s
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionOperationDto implements Serializable {

	private static final long serialVersionUID = -2791192626383260030L;

	private Account origin;

	private Account payee;
	
	private Double amountToTransfer;
}
