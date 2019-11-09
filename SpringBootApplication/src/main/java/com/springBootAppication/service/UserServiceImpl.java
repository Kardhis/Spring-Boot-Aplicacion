package com.springBootAppication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springBootAppication.entity.User;
import com.springBootAppication.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
	
	@Override
	public Iterable<User> getAllUsers() {
		return userRepository.findAll();
	}

}
