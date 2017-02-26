package com.epay.epayApp.service;

import java.util.Date;
import java.util.List;

import com.epay.apayApp.exception.TransactionException;
import com.epay.epayApp.entity.Account;
import com.epay.epayApp.entity.Account.Currency;
import com.epay.epayApp.entity.EpayUser;
import com.epay.epayApp.entity.TransactionHistory;
import com.epay.epayApp.entity.TransactionHistory.TraxnType;
import com.epay.epayApp.rest.dto.TransactionsDto;
import com.epay.epayApp.rest.dto.TransactionsStatusDto;
import com.epay.epayApp.rest.dto.UserBalanceDto;
import com.epay.epayApp.rest.dto.UserProfileDto;

/**
 * 
 * @author Asad Ali created on 20/02/2017
 *
 */

public interface TransactionsService {

	UserBalanceDto fetchBalance(EpayUser EpayUser);

	public TransactionsStatusDto purchase(EpayUser EpayUser, Double purchaseAmount, Currency currency,
			String description, Date date);

	public List<TransactionsDto> fetchTransactionHistory(EpayUser EpayUser);

	String addBalance(Currency inr, String description, EpayUser EpayUser, double amountToBeAdded, TraxnType credit,
			Account userAccount) throws TransactionException;

	Account CreateOrUpdateAccount(Currency currency, String description, EpayUser EpayUser, double balanceAmount,
			TraxnType traxnType, String transactionId, Date trxnDate, Account EpayUserAccount);

	TransactionHistory prepareTraxnHistory(Currency currency, String description, EpayUser EpayUser,
			Double purchaseamount, TraxnType traxnType, String transactionId, Date trxnDate, double balanceAmount,
			Account userAccount);

	EpayUser updateProfile(EpayUser user, String firstName, String lastname, Long mobileNumber, String email,
			String gender);

	TransactionsStatusDto prepareTransactionStatusDto(EpayUser epayUser, Account account, String traxnId, boolean b,
			TraxnType credit);

}
