package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name="user1")
public class User1 {
	
	@Id
	private String username;
	private String password;
	private String role;
	
	

}
