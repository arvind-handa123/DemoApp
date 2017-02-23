package com.epay.epayApp.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.spy.memcached.MemcachedClient;

import org.apache.commons.httpclient.HttpStatus;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.epay.epayApp.entity.User;
import com.epay.epayApp.repository.jpa.UserRepository;
import com.epay.epayApp.util.SecurityUtils;

/**
 * 
 * @author Asad Ali
 *
 */
@Configuration("apiSecurity")
@EnableWebSecurity
public class ApiSecurity implements HandlerInterceptor {
	@Resource
	MemcachedClient memcachedClient;

	@Autowired
	private UserRepository userRepository;
	private static Logger LOGGER = LoggerFactory.getLogger(ApiSecurity.class);
	private static final int SECRET_KEY_LENGHT = 16;
	
	private String[] urlToscape = { "/v1/login"};
	private ArrayList<String> escapUrl = new ArrayList<String>(Arrays.asList(urlToscape));

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// checks for header
		String pathInfo = request.getPathInfo();
		if (escapUrl.contains(pathInfo)) {
			LOGGER.debug("Escaping url {}", pathInfo);
			return true;
		}
		String encriptedAccessToken = request.getHeader("AUTH_ACCESS_TOKEN");
		String key = request.getHeader("TIME_OF_CALL");
		if (encriptedAccessToken != null && key != null && key.length() == SECRET_KEY_LENGHT) {
			return isAutherized(encriptedAccessToken, key, request, response);
		}
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		return false;

	}

	private boolean isAutherized(String encriptedAccessToken, String key, HttpServletRequest request,
			HttpServletResponse response) {
		String accessToken = SecurityUtils.decript(encriptedAccessToken, key);
		LOGGER.debug("Decrypted accessToken {}", accessToken);
		JSONObject userInfo = null;
		try {
			Future<Object> futureObject = memcachedClient.asyncGet(accessToken);
			Object object = futureObject.get(5, TimeUnit.SECONDS);
			LOGGER.debug("Fetching user info from memcached accessToken{} ", accessToken);
			if (object != null) {
				LOGGER.debug("User info{} found in memcached ", object.toString());
				userInfo = new JSONObject(object.toString());
				return isValidUser(userInfo, request, response);
			} else {
				LOGGER.debug("User info is not found in memacached check from db with token {}", accessToken);
				return isValidUser(lookupInDb(accessToken, response), request, response);
			}
		} catch (TimeoutException e) {
			LOGGER.error("Exception in authorizing user from access token " + accessToken, e);
			try {
				return isValidUser(lookupInDb(accessToken, response), request, response);
			} catch (IOException e1) {
				LOGGER.debug("Exception in validating user from access token " + accessToken, e1);
			}
		} catch (Exception e) {
			LOGGER.debug("Exception in authorizing user from access token " + accessToken, e);
		}

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		return false;
	}

	private JSONObject lookupInDb(String accessToken, HttpServletResponse res) {
		User user = userRepository.findByAccessToken(accessToken);
		if (user == null) {
			LOGGER.debug("User not found from access token {}", accessToken);
			res.setStatus(HttpStatus.SC_UNAUTHORIZED);
			return null;
		}

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("userId", user.getId());
		memcachedClient.set(user.getAccessToken(), 0, jsonObject.toString());

		LOGGER.debug("setting user info in memcached {}", jsonObject.toString());
		return jsonObject;
	}

	private boolean isValidUser(Object object, HttpServletRequest req, HttpServletResponse res) throws IOException {

		if (object == null) {
			return false;
		} else
			return true;

	}

	@Override
	public void postHandle(HttpServletRequest req, HttpServletResponse res, Object arg2, ModelAndView arg3)
			throws Exception {
		LOGGER.debug("post handler completed");
	}


	@Override
	public void afterCompletion(HttpServletRequest req, HttpServletResponse res, Object arg2, Exception ex)
			throws Exception {
		if (ex != null) {
			LOGGER.debug("path {}", req.getPathInfo());
			LOGGER.info("exception {} from controller, path {}", ex, req.getPathInfo());
		}
	}

}
