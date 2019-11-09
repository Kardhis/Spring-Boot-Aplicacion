package com.springBootAppication.service;

import javax.validation.Valid;

import com.springBootAppication.entity.User;

public interface UserService {

	public Iterable<User> getAllUsers();	// Iterable = significa que puede ser cualquier Coleccion.

	public User createUser(User user) throws Exception;
	
}
