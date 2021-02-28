package com.chisom.accountservice.repository;

import com.chisom.accountservice.model.SavingsAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Chisom.Iwowo
 */
@Repository
public interface SavingsAccountRepository extends JpaRepository<SavingsAccount, Long> {

    /**
     * find savings account by the username.
     *
     * @param username username
     * @param block    blocked account
     * @return SavingsAccount
     */
    @Query(value = "select * from savings_account where username=:username and block=:block",
            nativeQuery = true)
    Optional<SavingsAccount> findByUsername(String username, boolean block);
}
