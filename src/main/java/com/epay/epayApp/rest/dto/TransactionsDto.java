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

	public Double getAmountSpent() {
		return amountSpent;
	}

	public void setAmountSpent(Double amountSpent) {
		this.amountSpent = amountSpent;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	@Override
	public String toString() {
		return "TransactionsDto [date=" + date + ", description=" + description + ", amountSpent=" + amountSpent
				+ ", currency=" + currency + ", lastTransactionType=" + lastTransactionType + ", transactionId="
				+ transactionId + "]";
	}

	public TraxnType getLastTransactionType() {
		return lastTransactionType;
	}

	public void setLastTransactionType(TraxnType lastTransactionType) {
		this.lastTransactionType = lastTransactionType;
	}

	private Double amountSpent;
	private Currency currency;
	private TraxnType lastTransactionType;
	private String transactionId;
}
