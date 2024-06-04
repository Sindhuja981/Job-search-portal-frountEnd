package com.example.demo.entity;

import lombok.Data;

@Data
public class TestResult {
	
	private int testResultId;
	
	
	private JobSekers jobseker;
	
	private Questions question;
	
	
	private Vacancies vacancies;
	
	
	private String selectedOption;
	
	
	private String result;
	
	
	private String score;

}
