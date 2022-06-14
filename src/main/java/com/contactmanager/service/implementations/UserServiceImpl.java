package com.contactmanager.service.implementations;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.contactmanager.model.User;
import com.contactmanager.repository.UserRepository;
import com.contactmanager.service.interfaces.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;

	@Autowired
	public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {

		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	/* registering the user */

	public User registerUser(User user) {

		/* encrypting password using BCrypt */
		try {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("default.png");
			
			return userRepository.save(user);
		}
		catch(Exception e){
			
			System.out.println("Error in registering user");
		}
		
		return null;
		
	}

	/* fetching the details of particular user */

	public User getUserDetails(String email) {

		return userRepository.findByEmail(email);
	}
}
