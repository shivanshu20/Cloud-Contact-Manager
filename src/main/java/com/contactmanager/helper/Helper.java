package com.contactmanager.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.contactmanager.model.Contact;

import java.util.List;

@Service
public class Helper {

	private final EncryptionDecryption encryptDecrypt;

	@Autowired
	public Helper(EncryptionDecryption encryptDecrypt) {
		this.encryptDecrypt = encryptDecrypt;
	}

	/* helper method to decrypt all phoneNo of particular user by page */

	public Page<Contact> getDecryptedDetails(Page<Contact> allContacts) {

		for (Contact curContact : allContacts) {

			String encryptedPhoneNo = curContact.getPhoneNo();
			String secretKey = curContact.getSecretKey();

			String decryptedPhoneNo = encryptDecrypt.decrypt(encryptedPhoneNo, secretKey);

			curContact.setPhoneNo(decryptedPhoneNo);
		}

		return allContacts;
	}

	/* decryption for list of contacts*/
	
	public List<Contact> getDecryptedDetails(List<Contact> allContacts) {

		for (Contact curContact : allContacts) {

			String encryptedPhoneNo = curContact.getPhoneNo();
			String secretKey = curContact.getSecretKey();

			String decryptedPhoneNo = encryptDecrypt.decrypt(encryptedPhoneNo, secretKey);

			curContact.setPhoneNo(decryptedPhoneNo);
		}

		return allContacts;
	}
	
	
	/* decryption for single contact*/
	
	public Contact getDecryptedContact(Contact contact) {

		
		String encryptedPhoneNo = contact.getPhoneNo();
		String secretKey = contact.getSecretKey();
		
		String decryptedPhoneNo = encryptDecrypt.decrypt(encryptedPhoneNo, secretKey);
		
		contact.setPhoneNo(decryptedPhoneNo);
		
		return contact;
	}
}
