package com.example.demo.entity;


public class Company {
	
	private int companyId;
	
	
	private String companyName;
	
	
	private String regDate;
	
	
	private String email;
	
	private String mobile;
	
	private String location;

	private String city;

	private String state;
	
	private String companyLevel;
	
	private String url;
	
	private String logo;
	
	private String password;

	public Company() {
		super();
	}

	public Company(int companyId, String companyName, String regDate, String email, String mobile, String location,
			String city, String state, String companyLevel, String url, String logo, String password) {
		super();
		this.companyId = companyId;
		this.companyName = companyName;
		this.regDate = regDate;
		this.email = email;
		this.mobile = mobile;
		this.location = location;
		this.city = city;
		this.state = state;
		this.companyLevel = companyLevel;
		this.url = url;
		this.logo = logo;
		this.password = password;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCompanyLevel() {
		return companyLevel;
	}

	public void setCompanyLevel(String companyLevel) {
		this.companyLevel = companyLevel;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "company [companyId=" + companyId + ", companyName=" + companyName + ", regDate=" + regDate + ", email="
				+ email + ", mobile=" + mobile + ", location=" + location + ", city=" + city + ", state=" + state
				+ ", companyLevel=" + companyLevel + ", url=" + url + ", logo=" + logo + ", password=" + password + "]";
	}
	
	
	
}