package com.epay.epayApp.service;

import javax.servlet.http.HttpServletRequest;

import com.epay.epayApp.entity.EpayUser;

public interface UserService {
	public EpayUser findUser(HttpServletRequest request);
}
