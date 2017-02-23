package com.epay.epayApp.service;
/**
 * 
 * @author Asad Ali
 *
 */
public interface UnsubscribeUserService {

	void insertInDb(String content, String mailFrom, String source);

	boolean isAlreadyUnsubscribed(String content);

}
