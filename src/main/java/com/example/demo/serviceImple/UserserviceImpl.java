package com.example.demo.serviceImple;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Repository.UserRepository;
import com.example.demo.entity.User1;
import com.example.demo.exception.ResourceNotFoundException;
import com.exmaple.demo.service.UserService;

@Service
public class UserserviceImpl implements UserService{
	
	@Autowired
	UserRepository userRepo;

	@Override
	public User1 getUserByEmail(String username) {
		// TODO Auto-generated method stub
		Optional<User1> uu=  userRepo.findById(username);
		User1 user;
		if(uu.isPresent()) {
			user= uu.get();
		}else {
			throw new ResourceNotFoundException("user", "email", username);
		}
		return user;
	}

	@Override
	public boolean updateProfile(User1 user) {
		// TODO Auto-generated method stub
		Optional<User1> uu1 = userRepo.findById(user.getUsername());
		
		if(uu1.isPresent()) {
			userRepo.save(user);
			return true;
		}else {
		return false;
		}
	}
	

}
