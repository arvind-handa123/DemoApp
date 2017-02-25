package com.epay.epayApp.rest.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.epay.epayApp.entity.Account.Currency;
import com.epay.epayApp.entity.TransactionHistory.TraxnType;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class TransactionsStatusDto implements Serializable {

	@Override
	public String toString() {
		return "TransactionsStatusDto [date=" + date + ", status=" + status + ", transactions=" + transactions + "]";
	}

	private Date date;
	private String status;
	private List<TransactionsDto> transactions;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<TransactionsDto> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<TransactionsDto> transactions) {
		this.transactions = transactions;
	}
}
