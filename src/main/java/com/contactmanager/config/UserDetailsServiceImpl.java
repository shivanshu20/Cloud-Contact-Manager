package com.contactmanager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.contactmanager.model.User;
import com.contactmanager.repository.UserRepository;

public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		/* fetching user from database */
		
		User user = userRepository.findByEmail(email);
		
		
		/* checking if the user is registered or not*/
		
		if(user == null) {
			throw new UsernameNotFoundException("Could not found user!!");
		}
		
		
		UserDetailsImpl newUser = new UserDetailsImpl(user);
		
		return newUser;
	}

}
