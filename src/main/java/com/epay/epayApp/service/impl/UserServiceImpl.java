package com.epay.epayApp.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epay.epayApp.entity.EpayUser;
import com.epay.epayApp.repository.jpa.UserRepository;
import com.epay.epayApp.rest.dto.UserProfileDto;
import com.epay.epayApp.service.UserService;
import com.epay.epayApp.util.SecurityUtils;

/**
 * 
 * @author Asad Ali created on 19/02/2017
 *
 */

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;

	@Override
	public EpayUser findUser(HttpServletRequest request) {
		/**
		 * fetch auth Token and find out user from repository
		 */
		String accessToken = request.getHeader("AUTH_ACCESS_TOKEN");
		String key = request.getHeader("TIME_OF_CALL");
		EpayUser user = userRepository.findByAccessToken(SecurityUtils.decript(accessToken, key));
		return user;
	}

	@Override
	public UserProfileDto prepareUserDto(EpayUser epayUser) {
		UserProfileDto userProfileDto = new UserProfileDto();
		userProfileDto.setEmail(epayUser.getEmail());
		userProfileDto.setFirstName(epayUser.getFirstName());
		userProfileDto.setLastName(epayUser.getLastName());
		userProfileDto.setGender(epayUser.getGender());
		userProfileDto.setPhoneNumber(epayUser.getPhoneNumber());
		return userProfileDto;
	}

}
