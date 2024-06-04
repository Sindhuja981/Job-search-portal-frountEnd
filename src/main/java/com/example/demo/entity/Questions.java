package com.example.demo.entity;

import lombok.Data;

@Data
public class Questions {

	
	private int questionId;

	private Test test;

	private Vacancies vacancies;

	private String question;

	private String option1;

	private String option2;

	private String option3;

	private String option4;

	private String correct;

	private String score;
	

}
