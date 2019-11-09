package com.springBootAppication.service;

import com.springBootAppication.entity.User;

public interface UserService {

	public Iterable<User> getAllUsers();	// Iterable = significa que puede ser cualquier Coleccion.

	public User createUser(User user) throws Exception;
	
	public User getUserById(Long id) throws Exception;
	
	public User updateUser(User user) throws Exception;

	public void deleteUser(Long id) throws Exception;
	
	
}
