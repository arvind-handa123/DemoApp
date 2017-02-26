package com.epay.epayApp.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.epay.apayApp.exception.TransactionException;
import com.epay.epayApp.entity.Account;
import com.epay.epayApp.entity.Account.Currency;
import com.epay.epayApp.entity.EpayUser;
import com.epay.epayApp.entity.EpayUser.Gender;
import com.epay.epayApp.entity.TransactionHistory;
import com.epay.epayApp.entity.TransactionHistory.TraxnType;
import com.epay.epayApp.repository.jpa.AccountRepository;
import com.epay.epayApp.repository.jpa.TransactionHistoryRepository;
import com.epay.epayApp.repository.jpa.UserRepository;
import com.epay.epayApp.rest.dto.TransactionsDto;
import com.epay.epayApp.rest.dto.TransactionsStatusDto;
import com.epay.epayApp.rest.dto.UserBalanceDto;
import com.epay.epayApp.service.DbConfigService;
import com.epay.epayApp.service.TransactionsService;

/**
 * 
 * @author Asad Ali created on 19/02/2017
 *
 */

@Service
public class TransactionsServiceImpl implements TransactionsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private TransactionHistoryRepository transactionHistoryRepository;

	@Autowired
	Environment environment;

	@Autowired
	private DbConfigService dbConfigService;

	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionsService.class);

	@Override
	public UserBalanceDto fetchBalance(EpayUser user) {
		UserBalanceDto balanceDto = null;
		if (user != null) {
			balanceDto = new UserBalanceDto();
			Account account = accountRepository.findOne(user.getAccount().getId());
			balanceDto.setUserId(user.getId());
			balanceDto.setCurrentBalance(account.getBalanceAmount());
			balanceDto.setCurrencyCode(account.getCurrency().name());
			balanceDto.setLastTraxnType(account.getLastTransactionType());
		}

		return balanceDto;
	}

	@Override
	public List<TransactionsDto> fetchTransactionHistory(EpayUser user) {
		/**
		 * fetch list of transaction for the user from db and prepare dto
		 */
		List<TransactionHistory> transactions = transactionHistoryRepository.findByUserId(user.getId());
		List<TransactionsDto> transactionsHistoryDtosList = transactions.stream().map(f -> {
			TransactionsDto transactionsHistoryDto = new TransactionsDto();
			transactionsHistoryDto.setAmountSpent(f.getAmountSpent());
			transactionsHistoryDto.setCurrency(f.getCurrency());
			transactionsHistoryDto.setDescription(f.getDescription());
			transactionsHistoryDto.setDate(f.getTransactionDate());
			transactionsHistoryDto.setTransactionId(f.getTransactionId());
			transactionsHistoryDto.setLastTransactionType(f.getLastTransactionType());
			transactionsHistoryDto.setUserId(f.getUserId());
			return transactionsHistoryDto;

		}).collect(Collectors.toList());
		/*
		 * for (TransactionHistory transaction : transactions) { TransactionsDto
		 * transactionsHistoryDto = new TransactionsDto();
		 * transactionsHistoryDto.setAmountSpent(transaction.getAmountSpent());
		 * transactionsHistoryDto.setCurrency(transaction.getCurrency());
		 * transactionsHistoryDto.setDescription(transaction.getDescription());
		 * transactionsHistoryDto.setDate(transaction.getTransactionDate());
		 * transactionsHistoryDto
		 * .setTransactionId(transaction.getTransactionId());
		 * transactionsHistoryDto
		 * .setLastTransactionType(transaction.getLastTransactionType());
		 * transactionsHistoryDto.setUserId(transaction.getUserId());
		 * transactionsHistoryDtosList.add(transactionsHistoryDto); }
		 */

		return transactionsHistoryDtosList;
	}

	@Override
	public TransactionsStatusDto purchase(EpayUser user, Double purchaseAmount, Currency currency, String description,
			Date inputTraxnDate) {
		Account userAccount = accountRepository.findOne(user.getAccount().getId());
		if (userAccount != null) {
			double balanceAmount = userAccount.getBalanceAmount() - purchaseAmount;
			if (balanceAmount >= 0) {
				String transactionId = null;
				try {
					transactionId = persistTraxn(purchaseAmount, currency, description, user, balanceAmount,
							TraxnType.DEBIT, inputTraxnDate, userAccount);
				} catch (TransactionException e) {
					LOGGER.error("Exception occured while calling purchase ", e.getMessage());
				}
				/**
				 * prepare dto with transaction id and status
				 */
				return prepareTransactionStatusDto(user, userAccount, transactionId, true, TraxnType.DEBIT);
			} else {
				/**
				 * if balance sufficient balance is not available, return dto
				 * with insufficient balance message
				 */
				return prepareTransactionStatusDto(user, userAccount, null, false, TraxnType.DEBIT);
			}
		}
		return null;

	}

	@Override
	public TransactionsStatusDto prepareTransactionStatusDto(EpayUser user, Account userAccount, String transactionId,
			boolean isBalanceAmountAvailabale, TraxnType transactiontype) {
		TransactionsStatusDto statusDto = new TransactionsStatusDto();
		if (TraxnType.CREDIT.equals(transactiontype)) {
			return fetchStatus(statusDto, transactionId);

		} else if (TraxnType.DEBIT.equals(transactiontype)) {
			if (isBalanceAmountAvailabale) {
				return fetchStatus(statusDto, transactionId);
			} else {
				statusDto.setStatus(dbConfigService.getProperty("INSUFFICIENT_BALANCE",
						"Sorry! Insufficient balance, transaction could not be completed."));
			}
		}
		return statusDto;

	}

	private TransactionsStatusDto fetchStatus(TransactionsStatusDto statusDto, String transactionId) {
		if (transactionId != null && !transactionId.isEmpty()) {
			statusDto.setStatus(dbConfigService.getProperty("TRAXN_SUCCESS_MESSAGE",
					"Cheers! Your transaction was successful, and your transaction Id is : " + transactionId));
		} else {
			statusDto.setStatus(dbConfigService.getProperty("TRAXN_FAILURE_MESSAGE",
					"Oops! Something went wrong, your transaction was unsuccessful."));

		}
		return statusDto;

	}

	@Override
	public String addBalance(Currency currency, String description, EpayUser user, double amountToBeAdded,
			TraxnType traxnType, Account userAccount) {
		String traxnId = null;
		try {
			traxnId = persistTraxn(0.0, currency, description, user, amountToBeAdded, traxnType, null, userAccount);
			if (traxnId == null) {
				throw new TransactionException("Transaction was Unsuccessful");
			}
		} catch (TransactionException e) {
			LOGGER.error("Problem while adding balance to user = {} ", user.getId());
		}
		return traxnId;
	}

	@Transactional
	private String persistTraxn(Double purchaseamount, Currency currency, String description, EpayUser user,
			double balanceAmount, TraxnType traxnType, Date trxnDate, Account userAccount) throws TransactionException {
		String transactionId = prepareTraxnId(user);
		try {
			CreateOrUpdateAccount(currency, description, user, balanceAmount, traxnType, transactionId, trxnDate,
					userAccount);
			if (userAccount != null) {
				userAccount = accountRepository.saveAndFlush(userAccount);
			}
		} catch (Exception e) {
			LOGGER.error("Something went wrong, while persisting account, error={}, userId={}, transactionType={}",
					e.getMessage(), user.getId(), traxnType);
			throw new TransactionException(e.getMessage());

		}

		TransactionHistory transactionHistory = prepareTraxnHistory(currency, description, user, purchaseamount,
				traxnType, transactionId, trxnDate, balanceAmount);
		if (transactionHistory != null) {
			try {
				transactionHistoryRepository.save(transactionHistory);
			} catch (Exception e) {
				LOGGER.error(
						"Something went wrong, while savin transaction history call, error={}, userId={}, transactionType={}",
						e.getMessage(), user.getId(), traxnType);
				return null;
			}
		} else {
			return null;
		}
		return transactionId;
	}

	private String prepareTraxnId(EpayUser user) {
		if (user != null)
			return user.getId() + "" + new Date().getTime();
		return null;
	}

	@Override
	public TransactionHistory prepareTraxnHistory(Currency currency, String description, EpayUser user,
			Double purchaseamount, TraxnType traxnType, String transactionId, Date trxnDate, double balanceAmount) {
		TransactionHistory transactionHistory = new TransactionHistory();
		transactionHistory.setAmountSpent(purchaseamount);
		transactionHistory.setBalance(balanceAmount);
		transactionHistory.setCurrency(currency);
		transactionHistory.setDescription(description);
		if (trxnDate == null)
			trxnDate = new Date();
		transactionHistory.setTransactionDate(trxnDate);
		if (TraxnType.CREDIT == traxnType)
			transactionHistory.setLastTransactionType(TraxnType.CREDIT);
		else
			transactionHistory.setLastTransactionType(TraxnType.DEBIT);
		transactionHistory.setUserId(user.getId());
		transactionHistory.setTransactionId(transactionId);
		return transactionHistory;
	}

	@Override
	public Account CreateOrUpdateAccount(Currency currency, String description, EpayUser user, double balanceAmount,
			TraxnType traxnType, String transactionId, Date trxnDate, Account userAccount) {

		if (user != null) {
			if (userAccount == null)
				userAccount = new Account();

			userAccount.setCurrency(currency);
			userAccount.setDescription(description);
			if (trxnDate == null)
				trxnDate = new Date();
			userAccount.setUpdatedTime(trxnDate);
			userAccount.setTransactionId(transactionId);
			if (TraxnType.CREDIT == traxnType) {
				userAccount.setLastTransactionType(TraxnType.CREDIT);
				if ((userAccount.getBalanceAmount() != null))
					userAccount.setBalanceAmount(userAccount.getBalanceAmount() + balanceAmount);
				else
					userAccount.setBalanceAmount(balanceAmount);
			} else {
				userAccount.setLastTransactionType(TraxnType.DEBIT);
				userAccount.setBalanceAmount(balanceAmount);
			}
		}
		return userAccount;
	}

	@Override
	public EpayUser updateProfile(EpayUser user, String firstName, String lastname, Long mobileNumber, String email,
			String gender) {
		if (firstName != null && !firstName.isEmpty())
			user.setFirstName(firstName);
		if (lastname != null && !lastname.isEmpty())
			user.setLastName(lastname);
		if (email != null && !email.isEmpty())
			user.setEmail(email);
		try {
			user.setGender(Gender.valueOf(gender.toUpperCase()));
		} catch (Exception e) {
			LOGGER.error("Problem while setting gender, gender {} not found :", gender, e.getMessage());
		}
		if (mobileNumber != null)
			user.setPhoneNumber(mobileNumber);
		userRepository.save(user);
		return user;
	}
}
