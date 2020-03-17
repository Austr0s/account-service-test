package io.accountservice.test.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.accountservice.test.exception.CustomException;
import io.accountservice.test.exception.account.AccountNotFoundException;
import io.accountservice.test.model.dto.TransactionOperationDto;
import io.accountservice.test.model.entity.Account;
import io.accountservice.test.repository.AccountRepository;
import io.accountservice.test.service.AccountService;
import io.micrometer.core.lang.NonNull;

/**
 * Service Implementation of Account service to manage Accounts.
 * 
 * @author Austr0s
 */
@Service
@Transactional
public class AccountServiceImpl implements AccountService {

	/**
	 * AccountRepository to persist on database.
	 */
	@Autowired
	private AccountRepository repository;

	/**
	 * Method to get one Account by Id.
	 * 
	 * @param id of the Account that we want to return.
	 * @return optional account retrieved from database.
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public Optional<Account> findOne(@NonNull Long id) {
		return repository.findById(id);
	}

	/**
	 * Method to get all accounts as a list.
	 * 
	 * @return list of accounts retrieved from database.
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public List<Account> findAll() {
		return repository.findAll();
	}

	/**
	 * Method to create a new account.
	 * 
	 * @param entity to create and store on database.
	 * @return optional account saved on database.
	 * @throws CustomException error if an account with false Treasury value is
	 *                         trying to insert negative balance.
	 */
	@Override
	public Optional<Account> create(Account entity) throws CustomException {
		validateTreasureCreateAllow(entity);
		return Optional.of(repository.save(entity));
	}

	private void validateTreasureCreateAllow(Account entity) throws CustomException {
		if (!entity.getTreasury() && entity.getBalance() < 0)
			throw new CustomException(
					"Error Create: Account profile Treasury not allowed to create Account with negative Balande. Operation fails");
	}

	/**
	 * Method to update Account. This method validate if treasure was modified too.
	 * 
	 * @param entity Account to update values on Database.
	 * @return updated Account and saved on database.
	 * @throws CustomException if Treasury is changed on put request. Because id
	 *                         can't be changed after creation of Account.
	 */
	@Override
	public Account update(Account entity) throws CustomException {
		Account actual = findOne(entity.getId()).orElseThrow(() -> new AccountNotFoundException(
				String.format("Update Account Id: %s was not found", entity.getId())));

		if (actual != null && !entity.getTreasury().equals(actual.getTreasury()))
			throw new CustomException("Error: Treasury value changed. Operation fails");
		entity.setId(actual.getId());
		
		return repository.save(entity);
	}

	/**
	 * Method to delete Account.
	 * 
	 * @param id of account that we want to delete.
	 */
	@Override
	public void delete(Account entity) {
		repository.delete(entity);
	}

	/**
	 * Method to transfer balance from an Account to another Account. This method
	 * validates Treasury profile and if it could set balance to negative amount. It
	 * validates too if the Treasury value was changed after success transaction.
	 * 
	 * @param transaction dto to map values origin account and payee account.
	 * @return origin account
	 * @throws CustomException if Treasury is changed or profile doesn't accept
	 *                         negative balance.
	 */
	@Override
	public Optional<Account> transference(@NonNull TransactionOperationDto transaction) throws CustomException {
		Account origin = repository.getOne(transaction.getOrigin());

		validateTreasuryNotChanged(origin);
		validateTreasuryNegativeValid(origin, transaction.getAmountToTransfer());

		origin.setBalance(origin.getBalance() - transaction.getAmountToTransfer());
		Account payee = repository.getOne(transaction.getPayee());
		payee.setBalance(payee.getBalance() + transaction.getAmountToTransfer());

		repository.save(payee);
		repository.flush();

		return Optional.of(repository.save(origin));
	}

	/**
	 * Private Method to validate if an Account is allowed to realize transaction
	 * and save negative value on balance on it's balance.
	 * 
	 * @param origin account to validate.
	 * @param amount amount to subtract from the actual origin account balance.
	 * @throws CustomException
	 */
	private void validateTreasuryNegativeValid(Account origin, Double amount) throws CustomException {
		if (!origin.getTreasury() && calculateTransfer(origin.getBalance(), amount))
			throw new CustomException(
					"Error Transfer: Treasury profile doesn't accept negative balance on this profile. Operation fails");
	}

	/**
	 * Private Method to calculate balance result after decrement amount transfer
	 * 
	 * @param balance origin account balance.
	 * @param amount  amount to discount to actual account balance.
	 * @return true or false value depending on statement result.
	 */
	private boolean calculateTransfer(Double balance, Double amount) {
		return (balance - amount) < 0;
	}

	/**
	 * Private Methdo to validate if the origin value Treasury was changed. Because
	 * it can't be changed on update entity. Just setted only on creation of
	 * account.
	 * 
	 * @param origin account to treasury validate
	 * @throws CustomException exception if treasure was changed.
	 */
	private void validateTreasuryNotChanged(Account origin) throws CustomException {
		Account actual = repository.getOne(origin.getId());
		if (actual != null && !origin.getTreasury().equals(actual.getTreasury()))
			throw new CustomException("Error: Treasury value changed. Operation fails");
	}

}
