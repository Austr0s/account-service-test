package io.accountservice.test.service.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import io.accountservice.test.exception.CustomException;
import io.accountservice.test.exception.account.AccountNotFoundException;
import io.accountservice.test.model.dto.TransactionOperationDto;
import io.accountservice.test.model.entity.Account;
import io.accountservice.test.repository.AccountRepository;
import net.bytebuddy.utility.RandomString;

/**
 * Test class for AccountServiceImpl logic
 * 
 * @author Austr0s
 */
@SpringBootTest
public class AccountServiceImplTest {

	/**
	 * AccountRepository Mock
	 */
	@Mock
	private AccountRepository repository;

	/**
	 * AccountServiceImpl InjectMock. Service to Test with Mockito.
	 */
	@InjectMocks
	private AccountServiceImpl service;

	/**
	 * Private method to return random Long value.
	 * 
	 * @return random Log value.
	 */
	private Long getRandomLong() {
		return new Random().nextLong();
	}

	/**
	 * Method to return random String value.
	 * 
	 * @return random String value.
	 */
	private String getRandomString() {
		return RandomString.make(10);
	}

	/**
	 * {@link AccountServiceImpl#findOne(Long)}
	 */
	@Test
	public void testFindOne() {
		Long accountId = getRandomLong();
		Account expected = mock(Account.class);
		when(repository.findById(accountId)).thenReturn(Optional.of(expected));

		Account actual = service.findOne(accountId).orElse(expected);
		assertEquals(expected, actual);

		verify(repository, times(1)).findById(accountId);
	}

	/**
	 * {@link AccountServiceImpl#findAll()}
	 */
	@Test
	public void testFindAll() {
		Account account1 = mock(Account.class);
		Account account2 = mock(Account.class);
		Account account3 = mock(Account.class);
		List<Account> expected = Arrays.asList(account1, account2, account3);
		when(repository.findAll()).thenReturn(expected);

		List<Account> actual = service.findAll();
		assertEquals(expected, actual);

		verify(repository, times(1)).findAll();
	}

	/**
	 * This test must fail because Account isn't allowed to create negative Balance.
	 * 
	 * {@link AccountServiceImpl#create(io.accountservice.test.model.entity.Account)}
	 * 
	 * @throws CustomException
	 */
	@Test
	public void testCreateException() throws CustomException {
		Account entity = mock(Account.class);
		when(entity.getBalance()).thenReturn(Double.valueOf(-5000));
		when(entity.getCurrency()).thenReturn("EUR");
		when(entity.getName()).thenReturn(getRandomString());
		when(entity.getTreasury()).thenReturn(Boolean.FALSE);
		Account expected = mock(Account.class);
		when(repository.save(entity)).thenReturn(expected);

		assertThatThrownBy(() -> service.create(entity)) //
				.isInstanceOf(CustomException.class) //
				.hasMessage(
						"Error Create: Account profile Treasury not allowed to create Account with negative Balande. Operation fails")
				.hasNoCause();

	}

	/**
	 * This test allows to create an account with negative balance.
	 * 
	 * {@link AccountServiceImpl#create(io.accountservice.test.model.entity.Account)}
	 * 
	 * @throws CustomException
	 */
	@Test
	public void testCreateTreasuryAllowNegativeBalance() throws CustomException {
		Account entity = mock(Account.class);
		when(entity.getBalance()).thenReturn(Double.valueOf(-5000));
		when(entity.getCurrency()).thenReturn("EUR");
		when(entity.getName()).thenReturn(getRandomString());
		when(entity.getTreasury()).thenReturn(Boolean.TRUE);
		Account expected = mock(Account.class);
		when(repository.save(entity)).thenReturn(expected);

		Account actual = service.create(entity).orElse(expected);
		assertEquals(expected, actual);

		verify(repository, times(1)).save(entity);
	}

	/**
	 * This test is created by Not Allowed Treasury and Positive Balance.
	 * 
	 * {@link AccountServiceImpl#create(io.accountservice.test.model.entity.Account)}
	 * 
	 * @throws CustomException
	 */
	@Test
	public void testCreateTreasuryNotAllowedAndPositiveBalance() throws CustomException {
		Account entity = mock(Account.class);
		when(entity.getBalance()).thenReturn(Double.valueOf(5000));
		when(entity.getCurrency()).thenReturn("EUR");
		when(entity.getName()).thenReturn(getRandomString());
		when(entity.getTreasury()).thenReturn(Boolean.FALSE);
		Account expected = mock(Account.class);
		when(repository.save(entity)).thenReturn(expected);

		Account actual = service.create(entity).orElse(expected);
		assertEquals(expected, actual);

		verify(repository, times(1)).save(entity);
	}

	/**
	 * {@link AccountServiceImpl#update(io.accountservice.test.model.entity.Account)}
	 * 
	 * @throws CustomException
	 */
	@Test
	public void testUpdate() throws CustomException {
		Long entityId = getRandomLong();
		Account entity = mock(Account.class);
		when(entity.getId()).thenReturn(entityId);
		when(entity.getBalance()).thenReturn(Double.valueOf(5000));
		when(entity.getCurrency()).thenReturn("EUR");
		when(entity.getName()).thenReturn(getRandomString());
		when(entity.getTreasury()).thenReturn(Boolean.FALSE);

		Account expected = mock(Account.class);
		when(repository.save(entity)).thenReturn(expected);
		when(repository.findById(entityId)).thenReturn(Optional.of(expected));

		Account updated = mock(Account.class);
		when(repository.save(entity)).thenReturn(updated);
		Account actual = service.update(entity);
		assertEquals(updated, actual);

		verify(repository, times(1)).save(entity);
		verify(repository, times(1)).findById(entityId);

	}

	/**
	 * This method will throws exception
	 * {@link AccountServiceImpl#update(io.accountservice.test.model.entity.Account)}
	 * 
	 * @throws CustomException
	 */
	@Test
	public void testUpdateWithException() throws CustomException {
		Long entityId = getRandomLong();
		Account entity = mock(Account.class);
		when(entity.getId()).thenReturn(entityId);
		when(entity.getBalance()).thenReturn(Double.valueOf(5000));
		when(entity.getCurrency()).thenReturn("EUR");
		when(entity.getName()).thenReturn(getRandomString());
		when(entity.getTreasury()).thenReturn(Boolean.FALSE);

		Account expected = mock(Account.class);
		when(repository.save(entity)).thenReturn(expected);
		when(repository.getOne(entityId)).thenReturn(expected);

		when(entity.getTreasury()).thenReturn(Boolean.TRUE);

		assertThatThrownBy(() -> service.update(entity)) //
				.isInstanceOf(AccountNotFoundException.class) //
				.hasMessage(String.format("Update Account Id: %s was not found", entityId)) //
				.hasNoCause();
	}

	/**
	 * {@link AccountServiceImpl#delete(io.accountservice.test.model.entity.Account)}
	 */
	@Test
	public void testDelete() {
		Account entity = mock(Account.class);
		when(entity.getId()).thenReturn(getRandomLong());
		when(entity.getBalance()).thenReturn(Double.valueOf(5000));
		when(entity.getCurrency()).thenReturn("EUR");
		when(entity.getName()).thenReturn(getRandomString());
		when(entity.getTreasury()).thenReturn(Boolean.FALSE);

		Mockito.doNothing().when(repository).delete(entity);
		service.delete(entity);

		verify(repository, times(1)).delete(entity);
	}

	/**
	 * {@link AccountServiceImpl#transference(io.accountservice.test.model.dto.TransactionOperationDto)}
	 * @throws CustomException 
	 */
	@Test
	public void testTransference() throws CustomException {
		Long originId = getRandomLong();
		Account origin = mock(Account.class);
		when(origin.getId()).thenReturn(originId);
		when(origin.getBalance()).thenReturn(Double.valueOf(5000));
		when(origin.getCurrency()).thenReturn("EUR");
		when(origin.getName()).thenReturn(getRandomString());
		when(origin.getTreasury()).thenReturn(Boolean.FALSE);

		Long payeeId = getRandomLong();
		Account payee = mock(Account.class);
		when(payee.getId()).thenReturn(payeeId);
		when(payee.getBalance()).thenReturn(Double.valueOf(5000));
		when(payee.getCurrency()).thenReturn("EUR");
		when(payee.getName()).thenReturn(getRandomString());
		when(payee.getTreasury()).thenReturn(Boolean.FALSE);

		TransactionOperationDto transaction = mock(TransactionOperationDto.class);
		when(transaction.getOrigin()).thenReturn(originId);
		when(transaction.getPayee()).thenReturn(payeeId);
		when(transaction.getAmountToTransfer()).thenReturn(Double.valueOf(1500));

		Account expected = mock(Account.class);
		when(repository.save(origin)).thenReturn(expected);
		Account payeeExpected = mock(Account.class);
		when(repository.save(payee)).thenReturn(payeeExpected);
		when(repository.getOne(originId)).thenReturn(origin);
		when(repository.getOne(payeeId)).thenReturn(payee);
		
		Account actual = service.transference(transaction).orElse(origin);
		assertEquals(expected, actual);
		
		verify(repository, times(1)).save(origin);
		verify(repository, times(1)).save(payee);
		verify(repository, times(2)).getOne(originId);
		verify(repository, times(1)).getOne(payeeId);
	}

}
