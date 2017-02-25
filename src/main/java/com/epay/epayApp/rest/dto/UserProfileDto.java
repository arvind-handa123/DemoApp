package com.epay.epayApp.rest.dto;

import java.io.Serializable;

import com.epay.epayApp.entity.EpayUser.Gender;
import com.epay.epayApp.entity.TransactionHistory.TraxnType;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class UserProfileDto implements Serializable {

	private Long userId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	private Gender gender;
	private String email;
	private Long phoneNumber;
	private String firstName;
	@Override
	public String toString() {
		return "UserProfileDto [userId=" + userId + ", gender=" + gender + ", email=" + email + ", phoneNumber="
				+ phoneNumber + ", firstName=" + firstName + ", lastName=" + lastName + "]";
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(Long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	private String lastName;

}
