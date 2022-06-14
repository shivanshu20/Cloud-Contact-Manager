package com.contactmanager.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.contactmanager.model.Contact;
import com.contactmanager.model.User;
import com.contactmanager.service.implementations.ContactServiceImpl;
import com.contactmanager.service.implementations.SearchServiceImpl;
import com.contactmanager.service.implementations.UserServiceImpl;

@RestController
@RequestMapping("/cloudcontactmanager/user")
public class SearchController {

	private final UserServiceImpl userServiceImpl;
	private final SearchServiceImpl searchServiceImpl;

	@Autowired
	public SearchController(UserServiceImpl userServiceImpl, SearchServiceImpl searchServiceImpl) {

		this.userServiceImpl = userServiceImpl;
		this.searchServiceImpl = searchServiceImpl;
	}

	@GetMapping("/search/{query}")
	public ResponseEntity<?> searchContact(@PathVariable("query") String query, Principal principal) {

		User user = userServiceImpl.getUserDetails(principal.getName());
		List<Contact> contact = searchServiceImpl.searchContact(query, user);

		return ResponseEntity.ok(contact);
	}
}
