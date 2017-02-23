package com.epay.epayApp.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.epay.epayApp.entity.TransactionHistory;

/**
 * 
 * @author Asad Ali created on 20/02/2017
 *
 */

@Repository("transactionHistoryRepository")
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {

	List<TransactionHistory> findByUserId(Long id);

}
