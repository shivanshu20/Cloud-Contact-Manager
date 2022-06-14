package com.contactmanager.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.contactmanager.model.Contact;
import com.contactmanager.model.User;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long>{
	
	public Page<Contact> findByUser(User user, Pageable pageable);
	
	public List<Contact> findByUser(User user);
	
	public List<Contact> findByNameContainingAndUser(String name, User user);
	
}
