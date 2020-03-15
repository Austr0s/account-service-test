package io.accountservice.test.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.hateoas.RepresentationModel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Account Entity extends from RepresentationalModel to use Hateoas.
 * 
 * @author Austr0s
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@EqualsAndHashCode(callSuper = false, of = "id")
@Builder
@Entity
@Table(name = "ACCOUNT")
public class Account extends RepresentationModel<Account> implements Serializable {

	private static final long serialVersionUID = 1501960596815168782L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable = false)
	@Schema(description = "Unique identifier of the Account.", example = "1", required = true)
	private Long id;

	@NotNull
	@Column(name = "NAME", nullable = false)
	@Schema(description = "Name of the Account.", example = "Jessica Abigail", required = true)
	private String name;

	@NotNull
	@Column(name = "CURRENCY", nullable = false)
	@Schema(description = "Currency's type of the Account.", example = "EUR", required = true)
	private String currency;

	@NotNull
	@Column(name = "BALANCE", nullable = false)
	@Schema(description = "Balance of the Account.", example = "5000", required = true)
	private Double balance;

	@Column(name = "TREASURY", nullable = true)
	@Schema(description = "Treasury status type of the Account.", example = "true", required = true)
	private Boolean treasury;

	@Override
	public String toString() {
		String tostring = null;
		if (id != null)
			tostring = id.toString();
		return tostring;
	}
}
