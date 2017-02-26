package com.epay.epayApp.Controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.epay.apayApp.exception.TransactionException;
import com.epay.epayApp.entity.Account.Currency;
import com.epay.epayApp.entity.EpayUser;
import com.epay.epayApp.entity.TransactionHistory.TraxnType;
import com.epay.epayApp.repository.jpa.UserRepository;
import com.epay.epayApp.rest.dto.TransactionsDto;
import com.epay.epayApp.rest.dto.TransactionsStatusDto;
import com.epay.epayApp.rest.dto.UserBalanceDto;
import com.epay.epayApp.service.TransactionsService;
import com.epay.epayApp.service.UserService;
import com.epay.epayApp.util.JsonUtils;

/**
 * 
 * @author Asad Ali created on 19/02/2017
 *
 */

@Controller
@RequestMapping(value = "/v1")
public class TransactionsController {

	@Autowired
	private TransactionsService transactionsService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionsController.class);

	/**
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/balance", method = RequestMethod.GET)
	public ResponseEntity<?> getBalance(HttpServletRequest request) {
		LOGGER.info("/v1/balance request received : ");

		UserBalanceDto userBalanceDto = null;
		EpayUser user = userService.findUser(request);
		if (user != null)
			userBalanceDto = transactionsService.fetchBalance(user);
		else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(userBalanceDto, HttpStatus.OK);
	}

	/**
	 * 
	 * @param payload
	 * @param request
	 * @return
	 * 
	 * @throws TransactionException
	 * 
	 * 
	 *             Pass the payload(json) as string as request parameter, and
	 *             parse it perform purchase, and returns the transaction status
	 *             message whether the traxn was successful or failed
	 * 
	 */
	@RequestMapping(value = "/spend", method = RequestMethod.POST)
	public ResponseEntity<?> purchase(@RequestParam("payload") String payload, HttpServletRequest request)
			throws TransactionException {
		LOGGER.info("/v1/spend request received : ");
		TransactionsDto transactionsRequest = null;
		EpayUser user = userService.findUser(request);
		if (user == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		try {
			// deserialise payload i.e., json
			transactionsRequest = JsonUtils.deserializeEntity(payload, TransactionsDto.class);
		} catch (IOException e) {
			LOGGER.error("Problem while deserializing payload for token = {}", request.getHeader("AUTH_ACCESS_TOKEN"),
					e.getMessage());
		}
		TransactionsStatusDto transactionStatusDto = null;
		if (user != null && transactionsRequest != null) {
			transactionStatusDto = transactionsService.purchase(user, transactionsRequest.getDebitedAmount(),
					transactionsRequest.getCurrency(), transactionsRequest.getDescription(),
					transactionsRequest.getDate());
			return new ResponseEntity<>(transactionStatusDto, HttpStatus.OK);
		}
		return new ResponseEntity<>(transactionStatusDto, HttpStatus.BAD_REQUEST);

	}

	/**
	 * 
	 * @param request
	 * @return
	 * 
	 *         returns the number of transaction done by user with details like
	 *         date, description, type of transaction etc
	 */
	@RequestMapping(value = "/transactions", method = RequestMethod.GET)
	public ResponseEntity<?> fetchHistory(HttpServletRequest request) {
		LOGGER.info("/v1/transactions request received : ");
		EpayUser user = userService.findUser(request);
		List<TransactionsDto> transactionsHistoryDtoList = null;
		if (user != null)
			transactionsHistoryDtoList = transactionsService.fetchTransactionHistory(user);
		else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(transactionsHistoryDtoList, HttpStatus.OK);
	}

	/**
	 * 
	 * @param amount
	 * @param currency
	 * @param coupanCode
	 * @param email
	 * @param remark
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/recharge/wallet", method = RequestMethod.POST)
	public ResponseEntity<?> addBalance(@RequestParam("amount") Double amount,
			@RequestParam("currency") String currency,
			@RequestParam(name = "coupan", required = false) String coupanCode,
			@RequestParam(name = "email", required = false) String email,
			@RequestParam(name = "remark", required = false) String remark, HttpServletRequest request) {

		EpayUser epayUser = userService.findUser(request);
		if (epayUser == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		Currency userCurrency = null;
		String traxnId = null;
		LOGGER.info("/v1/recharge/wallet request received : ");
		try {
			userCurrency = Currency.valueOf(currency);
		} catch (Exception e) {
			LOGGER.error("problem while fetching currency, currency {} not found", currency, e.getMessage());
		}
		if (userCurrency == null)
			userCurrency = epayUser.getAccount().getCurrency();
		try {
			traxnId = transactionsService.addBalance(userCurrency, remark, epayUser, amount, TraxnType.CREDIT,
					epayUser.getAccount());
		} catch (TransactionException e) {
			LOGGER.error("Problem whille adding balance to wallet for user {} ", epayUser.getId(), e.getMessage());
		}
		TransactionsStatusDto transactionDto = transactionsService.prepareTransactionStatusDto(epayUser,
				epayUser.getAccount(), traxnId, false, TraxnType.CREDIT);
		return new ResponseEntity<>(transactionDto, HttpStatus.OK);
	}
}
