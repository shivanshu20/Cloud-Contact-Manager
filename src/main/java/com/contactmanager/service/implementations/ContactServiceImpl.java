package com.contactmanager.service.implementations;

import java.io.File;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.contactmanager.helper.EncryptionDecryption;
import com.contactmanager.model.Contact;
import com.contactmanager.model.User;
import com.contactmanager.repository.ContactRepository;
import com.contactmanager.service.interfaces.ContactService;

@Service
@Transactional
public class ContactServiceImpl implements ContactService {

	private final ContactRepository contactRepository;
	private final EncryptionDecryption encryptDecrypt;

	@Autowired
	public ContactServiceImpl(ContactRepository contactRepository, EncryptionDecryption encryptDecrypt) {

		this.contactRepository = contactRepository;
		this.encryptDecrypt = encryptDecrypt;
	}

	/* adding the new contact to database */

	@Override
	public Contact addContact(Contact contact, User user, MultipartFile imageFile) {

		
		try {
			/* encrypting the phoneNo before storing it into the database */
			
			String secretKey = encryptDecrypt.generateRandomString();
			String encryptedPhoneNo = encryptDecrypt.encrypt(contact.getPhoneNo(), secretKey);

			
			/* uploading image */
			boolean isUploaded = uploadImage(contact, user, imageFile);
			
			if(isUploaded == false)
				contact.setImage("contact.png");
			
			user.getContacts().add(contact);
			contact.setUser(user);
			contact.setPhoneNo(encryptedPhoneNo);
			contact.setSecretKey(secretKey);

			return contactRepository.save(contact);
		} 
		
		catch (Exception e) {
			
			System.out.println("Error in saving contact");
		}
		
		return null;
	}

	
	/* uploading image to the server */
	
	@Override
	public boolean uploadImage(Contact contact, User user, MultipartFile imageFile) {
		
		
		try {
			
			if(imageFile.isEmpty()) {
			
				contact.setImage("contact.png");
				return true;
			}
			
			else {
				
				System.out.println(contact.getcId() + " " + user.getId());
				
				contact.setImage(imageFile.getOriginalFilename());
				File saveFile = new ClassPathResource("static/images").getFile();
				
				Path fileLocation = Paths.get(saveFile.getAbsolutePath() + File.separator + imageFile.getOriginalFilename());
				Files.copy(imageFile.getInputStream(), fileLocation, StandardCopyOption.REPLACE_EXISTING);
				
				System.out.println("File is uploaded successfully !");
				
				return true;
			}
		}
		catch(Exception e) {
			
			System.out.println("Error in uploading image");
		}
		
		return false;
	}
	
	
	/* getting all the contacts for user by page wise*/

	@Override
	public Page<Contact> getContactsByPage(User user, Pageable pageable) {

		Page<Contact> contactsByPage = (Page<Contact>) contactRepository.findByUser(user, pageable);
		return contactsByPage;
	}
	
	
	/* downloading the contacts in CSV file */
	
	@Override
	public void exportContactsInCsv(List<Contact> allContacts, Writer writer) {
		
		try {
			
			ICsvBeanWriter csvWriter = new CsvBeanWriter(writer, CsvPreference.STANDARD_PREFERENCE);
			String csvHeader[] = {"Contact Id", "Name", "Email", "PhoneNo"};
			String nameMapping[] = {"cId", "name", "email", "phoneNo"};
			
			csvWriter.writeHeader(csvHeader);
			
			for(Contact c : allContacts) {
				
				csvWriter.write(c, nameMapping);
			}
			
			csvWriter.close();
			
		}
		catch(Exception e) {
			
			System.out.println("Error in exporting to csv");
		}
	}
	
	
	/* fetching all the contacts*/
	
	@Override
	public List<Contact> getAllContacts(User user) {
		
		return contactRepository.findByUser(user);
	}

	
	/* deleting the contact */
	@Override
	public void deleteContact(Long cId) {
		
		contactRepository.deleteById(cId);
	}
	
	
	/* finding the contact by particular id */
	
	@Override
	public Optional<Contact> getContactDetailsById(Long cId) {
		
		return contactRepository.findById(cId);
	}
	
	public Contact updateContact(Contact contact, User user, MultipartFile imageFile) {
		
		String secretKey = encryptDecrypt.generateRandomString();
		String encryptedPhoneNo = encryptDecrypt.encrypt(contact.getPhoneNo(), secretKey);
		
		boolean isUploaded = uploadImage(contact, user, imageFile);
		
		if(isUploaded == false)
			contact.setImage("contact.png");
		
		contact.setUser(user);
		contact.setPhoneNo(encryptedPhoneNo);
		contact.setSecretKey(secretKey);
		
		return contactRepository.save(contact);
	}
}
