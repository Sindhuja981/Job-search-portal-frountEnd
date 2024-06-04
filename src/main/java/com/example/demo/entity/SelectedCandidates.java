package com.example.demo.entity;

import lombok.Data;

@Data
public class SelectedCandidates {

	private int id;

	
	private Vacancies vacancies;

	
	private Test test;

	
	private JobSekers jobSekers;

	
	private int score;

	
	private String status;
}
