package com.epay.epayApp.rest.dto;

import java.io.Serializable;

import com.epay.epayApp.entity.TransactionHistory.TraxnType;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class UserBalanceDto implements Serializable {
	@Override
	public String toString() {
		return "UserBalanceDto  [userId=" + userId + ", currentBalance=" + currentBalance + ", currencyCode="
				+ currencyCode + ", lastTraxnType=" + lastTraxnType + "]";
	}

	private Long userId;
	private Double currentBalance;
	private String currencyCode;
	private TraxnType lastTraxnType;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Double getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(Double currentBalance) {
		this.currentBalance = currentBalance;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public TraxnType getLastTraxnType() {
		return lastTraxnType;
	}

	public void setLastTraxnType(TraxnType lastTraxnType) {
		this.lastTraxnType = lastTraxnType;
	}

}
