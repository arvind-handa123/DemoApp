package com.epay.epayApp.Controller;

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

import com.epay.epayApp.entity.EpayUser;
import com.epay.epayApp.service.LoginService;
import com.epay.epayApp.service.TransactionsService;
import com.epay.epayApp.service.UserService;

/**
 * 
 * @author Asad Ali created on 19/02/2017
 *
 */

@Controller
@RequestMapping(value = "/v1")
public class EpayUserController {

	@Autowired
	private LoginService loginService;

	@Autowired
	private UserService userService;

	@Autowired
	private TransactionsService transactionsService;

	private static final Logger LOGGER = LoggerFactory.getLogger(EpayUserController.class);

	/**
	 * 
	 * @param firstName
	 * @param lastname
	 * @param email
	 * @param mobileNumber
	 * @param gender
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/update/profile", method = RequestMethod.POST)
	public ResponseEntity<?> getBalance(@RequestParam(name = "firstName", required = false) String firstName,
			@RequestParam(name = "lastName", required = false) String lastname,
			@RequestParam(name = "email", required = false) String email,
			@RequestParam(name = "mobileNumber", required = false) Long mobileNumber,
			@RequestParam(name = "gender", required = false) String gender, HttpServletRequest request) {
		LOGGER.info("/v1/update/profile request received : ");

		EpayUser epayUser = userService.findUser(request);
		if (epayUser == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		epayUser = transactionsService.updateProfile(epayUser, firstName, lastname, mobileNumber, email, gender);
		return new ResponseEntity<>(userService.prepareUserDto(epayUser), HttpStatus.OK);
	}

}
