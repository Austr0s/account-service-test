package io.accountservice.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.accountservice.test.model.entity.Account;


/**
 * Repository of entity Account to realize changes on database and persist it.
 * 
 * @author Austr0s
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

}