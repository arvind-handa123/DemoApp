package com.epay.epayApp.Controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.epay.apayApp.exception.TransactionException;
import com.epay.epayApp.entity.EpayUser;
import com.epay.epayApp.repository.jpa.UserRepository;
import com.epay.epayApp.rest.dto.TransactionsDto;
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
			LOGGER.info("Problem while deserializing payload for token = {}", request.getHeader("AUTH_ACCESS_TOKEN"),
					e.getMessage());
		}
		String transactionStatus = null;
		if (user != null && transactionsRequest != null) {
			transactionStatus = transactionsService.purchase(user, transactionsRequest.getAmountSpent(),
					transactionsRequest.getCurrency(), transactionsRequest.getDescription(),
					transactionsRequest.getDate());
			if (transactionStatus != null && !transactionStatus.isEmpty())
				return new ResponseEntity<>(transactionStatus, HttpStatus.OK);
			else
				return new ResponseEntity<>(transactionStatus, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(transactionStatus, HttpStatus.BAD_REQUEST);

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
}
