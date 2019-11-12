package com.springBootAppication.service;

import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.springBootAppication.entity.Role;
import com.springBootAppication.repository.UserRepository;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		com.springBootAppication.entity.User appUser = userRepository.findByUsername(username).orElseThrow( () -> new UsernameNotFoundException("Login user inválido."));
		Set grantList = new HashSet();	// Contendrá los roles del usuario.
		
		for (Role role : appUser.getRoles()) {
			GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getDescription());
			grantList.add(grantedAuthority);
		}
		
		UserDetails userSession = (UserDetails) new User(username, appUser.getPassword(), grantList);
		
		return userSession;
	}

}
