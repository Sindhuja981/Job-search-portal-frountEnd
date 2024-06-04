package com.example.demo.entity;

import java.sql.Date;

import lombok.Data;

@Data
public class JobSekers {

	
	private int userId;
	private String firstName;
	private String lastName;
	private Date regDate;
	private String dateofbirth;
	private String gender;
	private String email;
	private String mobile;	
	private String address;
	private String password;
	private String profilePic;
	private String qualifications;
	private String experience;
	
}