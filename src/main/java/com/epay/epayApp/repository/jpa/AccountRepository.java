package com.epay.epayApp.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.epay.epayApp.entity.Account;
/**
 * 
 * @author Asad Ali created on 19/02/2017
 *
 */

@Repository("accountRepository")
public interface AccountRepository extends JpaRepository<Account, Long> {

}
