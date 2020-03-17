package io.accountservice.test.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.accountservice.test.model.dto.TransactionOperationDto;
import io.accountservice.test.model.entity.Account;
import io.accountservice.test.service.AccountService;

/**
 * AccountController Layer Test.
 * 
 * @author nit0
 */
@WebMvcTest(AccountController.class)
@ActiveProfiles("test")
public class AccountControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper mapper;

	@MockBean
	private AccountService service;
	
	@Test
	public void testGetOne() throws Exception {
		Account account = new Account(1L, "TEST", "EUR", Double.valueOf(5000), Boolean.FALSE);
		
		Mockito.when(service.findOne(1L)).thenReturn(Optional.of(account));
		
		mockMvc.perform(MockMvcRequestBuilders.get("/accounts/1")
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.name", is("TEST")))
				.andExpect(jsonPath("$.currency", is("EUR")))
				.andExpect(jsonPath("$.balance", is(5000.0)))
				.andExpect(jsonPath("$.treasury", is(false)));
	}
	
	@Test
	public void testGetAll() throws Exception {
		Account account1 = new Account(1L, "TEST", "EUR", Double.valueOf(5000), Boolean.FALSE);
		Account account2 = new Account(2L, "TEST2", "EUR", Double.valueOf(-1500), Boolean.TRUE);
		List<Account> accountList = Arrays.asList(account1, account2);
		
		Mockito.when(service.findAll()).thenReturn(accountList);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/accounts")
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8"))
				.andExpect(status().isOk())
				
				.andExpect(jsonPath("$._embedded.accountList[0].id", is(1)))
				.andExpect(jsonPath("$._embedded.accountList[0].name", is("TEST")))
				.andExpect(jsonPath("$._embedded.accountList[0].currency", is("EUR")))
				.andExpect(jsonPath("$._embedded.accountList[0].balance", is(5000.0)))
				.andExpect(jsonPath("$._embedded.accountList[0].treasury", is(false)))
				
				.andExpect(jsonPath("$._embedded.accountList[1].id", is(2)))
				.andExpect(jsonPath("$._embedded.accountList[1].name", is("TEST2")))
				.andExpect(jsonPath("$._embedded.accountList[1].currency", is("EUR")))
				.andExpect(jsonPath("$._embedded.accountList[1].balance", is(-1500.0)))
				.andExpect(jsonPath("$._embedded.accountList[1].treasury", is(true)));
	}

	@Test
	public void testCreate() throws Exception {
		Account account = new Account(1L, "TEST", "EUR", Double.valueOf(5000), Boolean.FALSE);
		
		Mockito.when(service.create(account)).thenReturn(Optional.of(account));
		
		mockMvc.perform(MockMvcRequestBuilders.post("/accounts")
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(mapper.writeValueAsString(account)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.name", is("TEST")))
				.andExpect(jsonPath("$.currency", is("EUR")))
				.andExpect(jsonPath("$.balance", is(5000.0)))
				.andExpect(jsonPath("$.treasury", is(false)));
	}
	
	@Test
	public void testUpdate() throws Exception {
		Account account = new Account(1L, "TEST", "EUR", Double.valueOf(5000), Boolean.FALSE);
		
		Mockito.when(service.update(account)).thenReturn(account);
		Mockito.when(service.findOne(1L)).thenReturn(Optional.of(account));
		
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
				.put("/accounts/{id}", 1L)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(mapper.writeValueAsBytes(account));
		
		mockMvc.perform(builder).andExpect(status()
				.isAccepted())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.name", is("TEST")))
				.andExpect(jsonPath("$.currency", is("EUR")))
				.andExpect(jsonPath("$.balance", is(5000.0)))
				.andExpect(jsonPath("$.treasury", is(false)));
	}
	
	@Test
	public void testDelete() throws Exception {
		Account account = new Account(1L, "TEST", "EUR", Double.valueOf(5000), Boolean.FALSE);

		AccountService serviceSpy = Mockito.spy(service);
		when(service.findOne(1L)).thenReturn(Optional.of(account));
		Mockito.doNothing().when(serviceSpy).delete(account);

		mockMvc.perform(MockMvcRequestBuilders.delete("/accounts/{id}", 1L)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());

		verify(service, times(1)).delete(account);
	}

	@Test
	public void testTransaction() throws JsonProcessingException, Exception {
		TransactionOperationDto transaction = new TransactionOperationDto(1L,2L,Double.valueOf(2000));
		Account originReturn = new Account(1L, "Test Name", "EUR", Double.valueOf(3000), Boolean.FALSE);
		Mockito.when(service.transference(transaction)).thenReturn(Optional.of(originReturn));
		
		mockMvc.perform(MockMvcRequestBuilders.post("/accounts/{originId}/payee/{payeeId}", 1L, 2L)
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(mapper.writeValueAsString(transaction)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.name", is("Test Name")))
				.andExpect(jsonPath("$.currency", is("EUR")))
				.andExpect(jsonPath("$.balance", is(3000.0)))
				.andExpect(jsonPath("$.treasury", is(false)));
	}

}
