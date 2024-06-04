package com.exmaple.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.entity.User1;

@Service
public interface UserService {
	User1 getUserByEmail (String username);
	boolean updateProfile(User1 user);

}
