package com.fwitter.models;

import java.sql.Date;

public class RegistrationObject {

	private String firstName;
	private String lastName;
	private String email;
	private Date dob;
	
	public RegistrationObject() {
		super();
	}

	public RegistrationObject(String firstName, String lastName, String email, Date dob) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.dob = dob;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	@Override
	public String toString() {
		return "RegistrationObject [firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", dob="
				+ dob + "]";
	}
	
	
}
