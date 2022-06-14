package com.contactmanager.service.implementations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.contactmanager.model.Contact;
import com.contactmanager.model.User;
import com.contactmanager.repository.ContactRepository;


@Service
public class SearchServiceImpl {
	
	private final ContactRepository contactRepository;
	
	@Autowired
	public SearchServiceImpl(ContactRepository contactRepository) {
		
		this.contactRepository = contactRepository;
	}
	public List<Contact> searchContact(String name, User user) {
		
		return contactRepository.findByNameContainingAndUser(name, user);
	}
}
