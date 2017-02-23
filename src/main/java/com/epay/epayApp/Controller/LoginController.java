package com.epay.epayApp.Controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.epay.epayApp.service.LoginService;

/**
 * 
 * @author Asad Ali created on 19/02/2017
 *
 */

@Controller
@RequestMapping(value = "/v1")
public class LoginController {

	@Autowired
	private LoginService loginService;

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> getAuthTokens() {
		LOGGER.info("/v1/login request received : ");
		Map<String, String> jsonResponse = loginService.generateAuthToken();
		if (jsonResponse != null && !jsonResponse.isEmpty())
			return new ResponseEntity<>(jsonResponse, HttpStatus.CREATED);
		else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

}
