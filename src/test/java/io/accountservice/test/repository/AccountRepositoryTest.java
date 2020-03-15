package io.accountservice.test.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.accountservice.test.model.entity.Account;

@SpringBootTest
@DisplayName("TEST Spring @Autowired AccountRepository")
public class AccountRepositoryTest {

	@Autowired
	private AccountRepository repository;

	@Test
	public void testFindAll() {
		List<Account> actual = repository.findAll();
		assertNotNull(actual);
	}

	@Test
	public void testGetOneAssertNull() {
		Account actual = repository.findById(new Random().nextLong()).orElse(null);
		assertNull(actual);
	}
	
	@Test
	public void testGetOneAssertNotNull() {
		Account actual = repository.findById(Long.valueOf(1)).orElse(null);
		assertNotNull(actual);
	}

	@Test
	public void testSave() {
		Account input = new Account();
		input.setBalance(Double.valueOf(-1000));
		input.setName("Test Name");
		input.setCurrency("EUR");
		input.setTreasury(Boolean.TRUE);
		
		Account actual = repository.save(input);
		assertNotNull(actual);
	}

	@Test
	public void testDelete() {
		Account input = new Account();
		input.setBalance(Double.valueOf(5000));
		input.setName("Test Name");
		input.setCurrency("EUR");
		input.setTreasury(Boolean.FALSE);
		
		Account actual = repository.save(input);
		
		repository.delete(actual);
		
		assertNotNull(actual);
		
	}

}
