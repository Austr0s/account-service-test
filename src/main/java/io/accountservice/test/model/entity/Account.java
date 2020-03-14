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
	private Long id;

	@NotNull
	@Column(name = "NAME", nullable = false)
	private String name;

	@NotNull
	@Column(name = "CURRENCY", nullable = false)
	private String currency;

	@NotNull
	@Column(name = "BALANCE", nullable = false)
	private Double balance;

	@Column(name = "TREASURY", nullable = true)
	private Boolean treasury;

	@Override
	public String toString() {
		String tostring = null;
		if (id != null)
			tostring = id.toString();
		return tostring;
	}
}
