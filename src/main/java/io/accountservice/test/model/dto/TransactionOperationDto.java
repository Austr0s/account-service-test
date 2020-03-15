package io.accountservice.test.model.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.hateoas.RepresentationModel;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TransactionOperationDto extends RepresentationModel<TransactionOperationDto> implements Serializable {

	private static final long serialVersionUID = -2791192626383260030L;

	@NotNull
	@Schema(description = "Origin Account of Transaction.", required = true)
	private Long origin;

	@NotNull
	@Schema(description = "Payee Account of Transaction.", required = true)
	private Long payee;

	@NotNull
	@Schema(description = "Amount to transfer from Origin Account to Payee Account", example = "1500", required = true)
	private Double amountToTransfer;
}
