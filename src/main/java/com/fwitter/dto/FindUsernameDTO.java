package com.fwitter.dto;

public class FindUsernameDTO {

	private String email;
	private String phone;
	private String Username;
	
	public FindUsernameDTO() {
		super();
	}
	
	public FindUsernameDTO(String email, String phone, String username) {
		super();
		this.email = email;
		this.phone = phone;
		Username = username;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getUsername() {
		return Username;
	}
	
	public void setUsername(String username) {
		Username = username;
	}
	
	@Override
	public String toString() {
		return "FindUsernameDTO [email=" + email + ", phone=" + phone + ", Username=" + Username + "]";
	}
	
}
