package com.epay.epayApp.service;

import java.util.Date;
import java.util.List;

import com.epay.apayApp.exception.TransactionException;
import com.epay.epayApp.entity.Account;
import com.epay.epayApp.entity.User;
import com.epay.epayApp.entity.Account.Currency;
import com.epay.epayApp.entity.TransactionHistory.TraxnType;
import com.epay.epayApp.rest.dto.TransactionsDto;
import com.epay.epayApp.rest.dto.UserBalanceDto;

/**
 * 
 * @author Asad Ali created on 20/02/2017
 *
 */

public interface TransactionsService {

	UserBalanceDto fetchBalance(User user);

	public String purchase(User user, Double purchaseAmount, Currency currency, String description, Date date);

	public List<TransactionsDto> fetchTransactionHistory(User user);

	String addBalance(Currency inr, String description, User user, double amountToBeAdded, TraxnType credit)
			throws TransactionException;

	Account CreateOrUpdateAccount(Currency currency, String description, User user, double balanceAmount,
			TraxnType traxnType, String transactionId, Date trxnDate, Account userAccount);

}
