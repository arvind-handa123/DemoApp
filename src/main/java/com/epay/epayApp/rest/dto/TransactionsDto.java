package com.epay.epayApp.rest.dto;

import java.io.Serializable;
import java.util.Date;

import com.epay.epayApp.entity.Account.Currency;
import com.epay.epayApp.entity.TransactionHistory.TraxnType;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class TransactionsDto implements Serializable {

	private Date date;
	private String description;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}


	@Override
	public String toString() {
		return "TransactionsDto [date=" + date + ", description=" + description + ", creditedAmount=" + creditedAmount
				+ ", debitedAmount=" + debitedAmount + ", currency=" + currency + ", lastTransactionType="
				+ lastTransactionType + ", transactionId=" + transactionId + ", userId=" + userId + "]";
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}


	

	public TraxnType getLastTransactionType() {
		return lastTransactionType;
	}

	public void setLastTransactionType(TraxnType lastTransactionType) {
		this.lastTransactionType = lastTransactionType;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Double getCreditedAmount() {
		return creditedAmount;
	}

	public void setCreditedAmount(Double creditedAmount) {
		this.creditedAmount = creditedAmount;
	}

	public Double getDebitedAmount() {
		return debitedAmount;
	}

	public void setDebitedAmount(Double debitedAmount) {
		this.debitedAmount = debitedAmount;
	}

	private Double creditedAmount;
	private Double debitedAmount;
	private Currency currency;
	private TraxnType lastTransactionType;
	private String transactionId;

	private Long userId;
}
