package com.springBootAppication.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.springBootAppication.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

	// Convención por configuración:
	// =============================
	
	// Buscará automáticamente en la BB.DD. el campo especificado tras el 'findBy':
	public Optional<User> findByUsername(String username);
	
	// Buscará automáticamente en la BB.DD. por los campos especificados tras el 'findBy':
	// Donde se devolverá el usuario que se haya encontrado o bien su username o bien su email.
	public Optional<User> findByUsernameOrEmail(String username, String email);
	
	// Buscará automáticamente en la BB.DD. por los campos especificados tras el 'findBy':
	// Donde se devolverá el usuario que se haya encontrado tanto su username como su email.
	public Optional<User> findByUsernameAndEmail(String username, String email);
	
}
