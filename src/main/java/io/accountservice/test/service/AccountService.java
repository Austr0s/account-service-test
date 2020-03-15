package io.accountservice.test.service;

import java.util.List;
import java.util.Optional;

import io.accountservice.test.exception.CustomException;
import io.accountservice.test.model.dto.TransactionOperationDto;
import io.accountservice.test.model.entity.Account;

/**
 * Interface to manage Accounts.
 * 
 * @author Austr0s
 *
 */
public interface AccountService {

	/**
	 * Method to get one Account by Id.
	 * 
	 * @param id of the Account that we want to return.
	 * @return optional account retrieved from database.
	 */
	Optional<Account> findOne(Long id);

	/**
	 * Method to get all accounts as a list.
	 * 
	 * @return list of accounts retrieved from database.
	 */
	List<Account> findAll();

	/**
	 * Method to create a new account.
	 * 
	 * @param entity to create and store on database.
	 * @return optional account saved on database.
	 * @throws CustomException error if an account with false Treasury value is
	 *                         trying to insert negative balance.
	 */
	Optional<Account> create(Account entity) throws CustomException;

	/**
	 * Method to update Account. This method validate if treasure was modified too.
	 * 
	 * @param entity Account to update values on Database.
	 * @return updated Account and saved on database.
	 * @throws CustomException if Treasury is changed on put request. Because id
	 *                         can't be changed after creation of Account.
	 */
	Account update(Account entity) throws CustomException;

	/**
	 * Method to delete Account.
	 * 
	 * @param id of account that we want to delete.
	 */
	void delete(Account entity);

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
	Optional<Account> transference(TransactionOperationDto transaction) throws CustomException;

}
