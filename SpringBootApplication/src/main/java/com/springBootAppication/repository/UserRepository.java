package com.springBootAppication.repository;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.springBootAppication.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

	// Convención por configuración:
	public Set<User> findByUsername(String username);	// Buscará automáticamente en la BB.DD. el campo especificado tras el 'findBy'.
	
}
