package io.accountservice.test.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.accountservice.test.exception.CustomException;
import io.accountservice.test.exception.account.AccountNotFoundException;
import io.accountservice.test.model.dto.TransactionOperationDto;
import io.accountservice.test.model.entity.Account;
import io.accountservice.test.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * RestController for Accounts
 * 
 * @author Austr0s
 */
@RestController
@RequestMapping(value = "/accounts")
@Tag(name = "Account", description = "The Account API")
@EnableHypermediaSupport(type = HypermediaType.HAL)
public class AccountController {

	/**
	 * AccountService to manage Accounts state
	 */
	@Autowired
	private AccountService service;

	@Operation(summary = "View an Account retrieved by Id", description = "Account", tags = { "Account" }) //
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Account.class)))) })
	@GetMapping(value = "/{id}", produces = { "application/hal+json" })
	public ResponseEntity<Account> getOne(@PathVariable(value = "id", required = true) Long id) {
		Account account = service.findOne(id)
				.orElseThrow(() -> new AccountNotFoundException("Get One Account Id: " + id + " was not found"));
		setOneLink(account);
		return ResponseEntity.ok(account);
	}

	@Operation(summary = "View a list of available Accounts", description = "Collection of Accounts", tags = {
			"Account" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Account.class)))) })
	@GetMapping(produces = { "application/hal+json" })
	public CollectionModel<Account> getAll() {
		List<Account> accounts = service.findAll();
		if (accounts == null)
			throw new CustomException("The resource you were trying to reach is not found");
		accounts.stream().forEach(x -> setOneLink(x));

		Link link = linkTo(AccountController.class).withSelfRel();
		CollectionModel<Account> result = new CollectionModel<>(accounts, link);

		return result;
	}

	@Operation(summary = "Create an Account", description = "Account", tags = { "Account" }) //
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Account.class)))) })
	@PostMapping(produces = { "application/hal+json" })
	public ResponseEntity<Account> create(@Valid @RequestBody(required = true) Account entity) {
		Account account = service.create(entity).orElseThrow(
				() -> new CustomException("Create Account: Some thing went wrong creating" + entity.getName()));
		setOneLink(account);

		return new ResponseEntity<Account>(account, HttpStatus.CREATED);
	}

	@Operation(summary = "Update an Account", description = "Account", tags = { "Account" }) //
	@ApiResponses(value = {
			@ApiResponse(responseCode = "202", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Account.class)))) })
	@PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Account> update(@PathVariable Long id, @Valid @RequestBody(required = true) Account entity) {
		if (!entity.getId().equals(id))
			throw new CustomException(
					String.format("Update Account Id: %s isn't the same of Account to update: %s", id, entity.getId()));

		Account responseAccount = service.findOne(id).orElseThrow(
				() -> new AccountNotFoundException(String.format("Update Account Id: %s was not found", id)));

		entity.setId(responseAccount.getId());
		Account updatedAccount = service.update(entity);
		setOneLink(updatedAccount);

		return new ResponseEntity<Account>(updatedAccount, HttpStatus.ACCEPTED);
	}

	@Operation(summary = "Delete an Account", description = "Account", tags = { "Account" }) //
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Account.class)))) })
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Account accountToDelete = service.findOne(id).orElseThrow(
				() -> new AccountNotFoundException(String.format("Delete Account Id: %s was not found", id)));

		service.delete(accountToDelete);

		return new ResponseEntity<>(HttpStatus.valueOf(204));
	}

	@Operation(summary = "Transaction between Accounts", description = "Account", tags = { "Account" }) //
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Account.class)))) })
	@PostMapping(value = "/{originId}/payee/{payeeId}", produces = { "application/hal+json" })
	public ResponseEntity<Account> transaction(@Valid @RequestBody(required = true) TransactionOperationDto transaction,
			@PathVariable Long originId, @PathVariable Long payeeId) {
		if (!transaction.getOrigin().equals(originId))
			throw new CustomException(String.format(
					"Transaction - Origin Account Id: %s isn't the same of Account to sustract balance id: %s",
					originId, transaction.getOrigin()));

		if (!transaction.getPayee().equals(payeeId))
			throw new CustomException(
					String.format("Transaction - Payee Account Id: %s isn't the same of Account to add balance id: %s",
							payeeId, transaction.getPayee()));

		Account responseAccountOrigin = service.transference(transaction)
				.orElseThrow(() -> new CustomException(
						String.format("Transaction - Origin Account Id: %s  and Payee Id: %s. Something went wrong.",
								originId, payeeId)));
		setOneLink(responseAccountOrigin);

		return ResponseEntity.ok(responseAccountOrigin);
	}

	private void setOneLink(Account account) {
		String accountId = String.format("accounts/%s", account.getId());
		Link selfLink = linkTo(Account.class).slash(accountId).withSelfRel();
		account.add(selfLink);
	}

}
