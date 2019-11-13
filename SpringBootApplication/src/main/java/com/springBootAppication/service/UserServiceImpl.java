package com.springBootAppication.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.springBootAppication.Exception.UsernmeOrIdNotFoundException;
import com.springBootAppication.dto.ChangePasswordForm;
import com.springBootAppication.entity.User;
import com.springBootAppication.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public Iterable<User> getAllUsers() {
		return userRepository.findAll();
	}

	private boolean checkUsernameAvailable(User user) throws Exception {
		Optional<User> userFound = userRepository.findByUsername(user.getUsername());
		
		if (userFound.isPresent()) {
			throw new Exception("Username no disponible.");
		}
		
		return true;
	}
	
	private boolean checkPasswordValid(User user) throws Exception {
		if (user.getConfirmPassword() == null || user.getConfirmPassword().isEmpty()) {
			throw new Exception("Confirm Password es obligatorio.");
		}
		
		if (!user.getPassword().equals(user.getConfirmPassword())) {
			throw new Exception("Password y Confirm Password no son iguales.");
		}
		
		return true;
	}

	@Override
	public User createUser(User user) throws Exception {
		if (checkUsernameAvailable(user) && checkPasswordValid(user)) {
			String encodePassword = bCryptPasswordEncoder.encode(user.getPassword());
			user.setPassword(encodePassword);
			
			user = userRepository.save(user);
		}
		
		return user;
	}

	@Override
	public User getUserById(Long id) throws UsernmeOrIdNotFoundException {
		return userRepository.findById(id).orElseThrow( () -> new UsernmeOrIdNotFoundException("El Id del usuario no existe."));
	}

	@Override
	public User updateUser(User fromUser) throws Exception {
		User toUser = getUserById(fromUser.getId());
		mapUser(fromUser, toUser);
		return userRepository.save(toUser);
	}
	
	/**
	 * Map everything, but password.
	 * @param from
	 * @param to
	 */
	protected void mapUser(User from, User to) {
		to.setUsername(from.getUsername());
		to.setFirstName(from.getFirstName());
		to.setLastName(from.getLastName());
		to.setEmail(from.getEmail());
		to.setRoles(from.getRoles());
	}

	@Override
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	public void deleteUser(Long id) throws UsernmeOrIdNotFoundException {
		User user = getUserById(id);
		userRepository.delete(user);
	}

	@Override
	public User changePassword(ChangePasswordForm form) throws Exception {
		User user = getUserById(form.getId());
		
		// Verificar si el current password ingresado es diferente al de la BB.DD. :
		if (!isLoggedUserADMIN() && !user.getPassword().equals(form.getCurrentPassword())) {
			throw new Exception("Current password inválido.");
		}
		
		// Verificar si el nuevo password ingresado es igual al de la BB.DD. :
		if (user.getPassword().equals(form.getNewPassword())) {
			throw new Exception("El nuevo password tiene que ser diferente del actual.");
		}
		
		// Verificar que el nuevo password ingresado no sea diferente que la confirmación del password ingresado:
		if (!form.getNewPassword().equals(form.getConfirmPassword())) {
			throw new Exception("El nuevo password y la confirmación del nuevo password no coinciden.");
		}
		
		String encodePassword = bCryptPasswordEncoder.encode(form.getNewPassword());
		user.setPassword(encodePassword);
		
		return userRepository.save(user);
	}
	
	public boolean isLoggedUserADMIN(){
		return loggedUserHasRole("ROLE_ADMIN");
	}

	public boolean loggedUserHasRole(String role) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDetails loggedUser = null;
		Object roles = null;
		
		if (principal instanceof UserDetails) {
			loggedUser = (UserDetails) principal;
		
			roles = loggedUser.getAuthorities().stream()
					.filter(x -> role.equals(x.getAuthority()))      
					.findFirst().orElse(null); // loggedUser = null;
		}
		
		return roles != null ? true : false;
	}
	
}
