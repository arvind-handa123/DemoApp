package com.epay.epayApp.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.epay.epayApp.entity.TransactionHistory.TraxnType;

/**
 * 
 * @author Asad Ali created on 19/02/2017
 *
 */

@Entity
public class Account implements Serializable {
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	public enum Currency {
		INR, DOLLAR, EURO, DINAR, DIRHAM, OTHER;
	}

	private TraxnType lastTransactionType;

	private Currency currency;
	private Double balanceAmount;

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
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

	public Double getBalanceAmount() {
		return balanceAmount;
	}

	public void setBalanceAmount(Double balanceAmount) {
		this.balanceAmount = balanceAmount;
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

	private Date updatedTime;
	private String description;
	private String transactionId;

}