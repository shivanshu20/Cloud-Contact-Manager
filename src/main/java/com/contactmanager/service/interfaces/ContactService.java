package com.contactmanager.service.interfaces;

import java.io.Writer;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.web.multipart.MultipartFile;

import com.contactmanager.model.Contact;
import com.contactmanager.model.User;

public interface ContactService {
	
	/* adding the contact to database */
	public Contact addContact(Contact contact, User user, MultipartFile imageFile);
	
	
	/* getting all the contacts for user by page*/
	public Page<Contact> getContactsByPage(User user, Pageable pageble);
	
	
	/* uploading image to the server */
	public boolean uploadImage(Contact contact, User user, MultipartFile imageFile);
	
	
	/* getting all the contacts for user*/
	public List<Contact> getAllContacts(User user);
	
	
	/* export the contacts in CSV file */
	public void exportContactsInCsv(List<Contact> allContacts, Writer writer);
	
	
	/* deleting the contact */
	public void deleteContact(Long cId);
	
	
	/* getting the details of contact */
	public Optional<Contact> getContactDetailsById(Long cId);
}
