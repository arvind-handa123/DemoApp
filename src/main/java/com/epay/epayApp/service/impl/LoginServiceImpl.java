package com.epay.epayApp.service.impl;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import net.spy.memcached.MemcachedClient;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.epay.apayApp.exception.TransactionException;
import com.epay.epayApp.entity.Account;
import com.epay.epayApp.entity.Account.Currency;
import com.epay.epayApp.entity.EpayUser;
import com.epay.epayApp.entity.TransactionHistory;
import com.epay.epayApp.entity.TransactionHistory.TraxnType;
import com.epay.epayApp.repository.jpa.AccountRepository;
import com.epay.epayApp.repository.jpa.TransactionHistoryRepository;
import com.epay.epayApp.repository.jpa.UserRepository;
import com.epay.epayApp.service.DbConfigService;
import com.epay.epayApp.service.LoginService;
import com.epay.epayApp.service.TransactionsService;
import com.epay.epayApp.util.SecurityUtils;

/**
 * 
 * @author Asad Ali created on 19/02/2017
 *
 */

@Service
@PropertySource(value = "classpath:/epayDetails.properties")
public class LoginServiceImpl implements LoginService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	TransactionsService transactionsService;

	@Autowired
	Environment environment;

	@Autowired
	MemcachedClient memcachedClient;

	@Autowired
	private TransactionHistoryRepository transactionHistoryRepository;

	@Autowired
	private DbConfigService dbConfigService;

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginServiceImpl.class);

	@Override
	public Map<String, String> generateAuthToken() {
		Map<String, String> authTokenMap = new HashMap<String, String>();
		String authToken = UUID.randomUUID().toString();
		try {
			EpayUser user = createUser(authToken);
			if (user != null) {
				LOGGER.info("User and account created with token = {} and user = {} at {}", authToken, user.getId());
				authTokenMap.put("AUTH_ACCESS_TOKEN", SecurityUtils.encript(authToken));
				authTokenMap.put("TIME_OF_CALL", SecurityUtils.API_SECRET_KEY);
				LOGGER.info("Access token = {} created for user = {}", user.getAccessToken(), user.getId());
				updateMemcache(user);
			}
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException | TransactionException e) {
			LOGGER.error("Problem while creating user or adding balance {} ", e.getMessage());
			return null;

		}

		return authTokenMap;
	}

	private void updateMemcache(EpayUser user) {
		/**
		 * Cache access Token as key value pair in memcache
		 */
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("userId", user.getId());
		memcachedClient.set(user.getAccessToken(), 0, jsonObject.toString());
		// System.out.println(jsonObject.toString());
		LOGGER.debug("setting user info in memcached {}", jsonObject.toString());
	}

	@Transactional
	private EpayUser createUser(String authToken) throws TransactionException {
		EpayUser user = null;
		try {
			user = new EpayUser();
			user.setAccessToken(authToken);
			user.setAccount(createAccount(user));
			user = userRepository.save(user);
			updateTransactionHistory(user);
		} catch (Exception e) {
			LOGGER.error("Problem while creating user for token = {}", authToken, e.getMessage());
			return null;
		}
		return user;

	}

	private Account createAccount(EpayUser user) throws TransactionException {
		String presetCurrency = dbConfigService.getProperty("PRESET_CURRENCY", "INR");
		Currency currency = Currency.valueOf(presetCurrency);
		String description = dbConfigService.getProperty("PRESET_DESCRIPTION",
				"Welcome offer! preset Balance and currency added to account.");
		Double presetAmount = dbConfigService.getDoubleProperty("PRESET_AMOUNT", 99.0);
		/**
		 * set transaction id as SYSTEM, whenever preset amount added.
		 */
		String system = dbConfigService.getProperty("PRESET_TRXNID", "SYSTEM");
		Account account = transactionsService.CreateOrUpdateAccount(currency, description, user, presetAmount,
				TraxnType.CREDIT, system, null, null);
		return account;
	}

	private void updateTransactionHistory(EpayUser user) throws TransactionException {
		/**
		 * update credited preset amount in transaction history,
		 * 
		 */
		if (user != null) {
			Account account = user.getAccount();
			try {
				TransactionHistory transactionHistory = transactionsService.prepareTraxnHistory(account.getCurrency(),
						account.getDescription(), user, 0.0, TraxnType.CREDIT, account.getTransactionId(), new Date(),
						account.getBalanceAmount());
				if (transactionHistory != null) {
					transactionHistoryRepository.save(transactionHistory);
				}

			} catch (Exception e) {
				LOGGER.error(
						"Something went wrong, while savin transaction history call, error={}, userId={}, transactionType={}",
						e.getMessage(), user.getId(), TraxnType.CREDIT);
				throw new TransactionException(e.getMessage());
			}

		}
	}
}
