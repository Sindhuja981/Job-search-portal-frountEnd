package com.example.demo.entity;

import java.sql.Date;

import lombok.Data;

@Data
public class JobApply {
	
private int applyId;

	
	private JobSekers jobsekers;

	
	private Vacancies vacancies;

	
	private String applyDate;
	private String status;
	private String finalscore;
}
