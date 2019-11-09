package com.springBootAppication.service;

import com.springBootAppication.entity.User;

public interface UserService {

	public Iterable<User> getAllUsers();	// Iterable = significa que puede ser cualquier Coleccion.
	
}
