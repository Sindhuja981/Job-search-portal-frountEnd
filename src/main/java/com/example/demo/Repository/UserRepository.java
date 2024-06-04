package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.example.demo.entity.User1;

public interface UserRepository extends JpaRepository<User1,String>{

}
