package com.example.demo.entity;

import lombok.Data;

@Data
public class Vacancies {
	
private int vacancyId;
	
	private Company Company;
	
	
	private String postDate;
	
	
	private String jobTitle;
	
	
	private String description;
	
	
	private String requirements;
	
	
	private int  noOfVancancies;
	
	
	private String OpenDate;
	
	
	private String closeDate;

}
