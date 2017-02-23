package com.epay.epayApp.service;

import javax.servlet.http.HttpServletRequest;

import com.epay.epayApp.entity.User;

public interface UserService {
	public User findUser(HttpServletRequest request);
}
