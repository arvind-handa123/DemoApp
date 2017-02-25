package com.epay.epayApp.service;

import javax.servlet.http.HttpServletRequest;

import com.epay.epayApp.entity.EpayUser;
import com.epay.epayApp.rest.dto.UserProfileDto;

public interface UserService {
	public EpayUser findUser(HttpServletRequest request);

	public UserProfileDto prepareUserDto(EpayUser epayUser);
}
