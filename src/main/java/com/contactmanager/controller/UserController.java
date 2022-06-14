package com.contactmanager.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.contactmanager.helper.Helper;
import com.contactmanager.helper.Message;
import com.contactmanager.model.Contact;
import com.contactmanager.model.User;
import com.contactmanager.service.implementations.ContactServiceImpl;
import com.contactmanager.service.implementations.UserServiceImpl;

import net.bytebuddy.asm.Advice.This;

@Controller
@RequestMapping("/cloudcontactmanager/user")
public class UserController {

	private final String modelMessage = "User - ";

	private final UserServiceImpl userServiceImpl;
	private final ContactServiceImpl contactServiceImpl;
	private final Helper helper;

	private User user;

	@Autowired
	public UserController(UserServiceImpl userServiceImpl, ContactServiceImpl contactServiceImpl, Helper helper) {

		this.userServiceImpl = userServiceImpl;
		this.contactServiceImpl = contactServiceImpl;
		this.helper = helper;
	}

	
	/* fetching email of logged in user */

	@ModelAttribute
	public void getUserDetails(Model model, Principal principal) {

		this.user = userServiceImpl.getUserDetails(principal.getName());

		model.addAttribute("user", user);
	}

	
	/* user's home page after login */

	@RequestMapping("/dashboard")
	public String home(Model model) {

		model.addAttribute("title", modelMessage + "Dashboard");
		return "normal/user_dashboard";
	}

	
	/* add-contact form page */

	@GetMapping("/add-contact")
	public String showContactForm(Model model) {

		model.addAttribute("title", modelMessage + "Add Contact");
		model.addAttribute("contact", new Contact());

		return "normal/add_contact_form";
	}

	
	/* add contact */

	@PostMapping("/process-contact")
	public String processContactDetails(@ModelAttribute("contact") Contact contact,
			@RequestParam("profileImage") MultipartFile imageFile, Model model) {

		Contact savedContact = contactServiceImpl.addContact(contact, user, imageFile);

		if (savedContact != null) {

			model.addAttribute("message", new Message("Contact added successfully!", "success"));
		} 
		
		else {
			model.addAttribute("message", new Message("Something went wrong Please try again!", "danger"));
		}

		return "normal/add_contact_form";
	}

	
	
	/* fetching all the contacts by page*/

	
	/* per page = 5  and current page = 0*/
	
	
	@GetMapping("/show-contacts/{page}")
	public String getAllContacts(@PathVariable("page") Integer page, Model model) {

		
		Pageable pageable = PageRequest.of(page, 4);
		
		Page<Contact> allContacts = contactServiceImpl.getContactsByPage(user, pageable);
		allContacts = helper.getDecryptedDetails(allContacts);
		
		
		model.addAttribute("title", modelMessage + "View contacts");
		model.addAttribute("allContacts", allContacts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", allContacts.getTotalPages());
	
		return "normal/view_contacts";
	}
	
	
	
	/* downloading the contacts */
	
	@GetMapping("/export-contacts")
	public void exportContacts(HttpServletResponse servletResponse, Model model) throws IOException{
		
		List<Contact> allContacts = contactServiceImpl.getAllContacts(user);
		allContacts = helper.getDecryptedDetails(allContacts);
		
		servletResponse.setContentType("text/csv");
		servletResponse.addHeader("Content-Disposition","attachment; filename=\"contacts.csv\"");
		contactServiceImpl.exportContactsInCsv(allContacts, servletResponse.getWriter());
		
	}
	
	
	/* deleting the particular contact */
	
	@GetMapping("/delete-contact/{cId}")
	public String deleteContact(@PathVariable("cId") Long cId, Model model) {
		
		Optional<Contact> optionalContactDetails = contactServiceImpl.getContactDetailsById(cId);
		
		Contact contactDetails = optionalContactDetails.isEmpty() ? null : optionalContactDetails.get();
		
		
		if(contactDetails == null)
			return "normal/error";
		
		else if(contactDetails.getUser().getId() != this.user.getId())
			return "normal/error";
		
		
		contactServiceImpl.deleteContact(cId);
		
		model.addAttribute("message", new Message("Contact deleted successfully!", "success"));
		
		return "redirect:/cloudcontactmanager/user/show-contacts/0";
	}
	
	
	/* update contact-form*/
	
	@GetMapping("/update-contact/{cId}")
	public String showupdateContactForm(@PathVariable("cId") Long cId, Model model) {
		
		Optional<Contact> optionalContact = contactServiceImpl.getContactDetailsById(cId);
		
		Contact contact = optionalContact.isEmpty() ? null : optionalContact.get();
		
		if(contact == null)
			return "normal/error";
		
		
		/* for decryption of phoneNo*/
		
		contact = helper.getDecryptedContact(contact);
		
		model.addAttribute("contact", contact);
		return "normal/update_contact_form";
	}
	
	
	/* updating contact details of user */
	
	@PostMapping("/process-contact-update")
	public String updateContactDetails(@ModelAttribute("contact") Contact contact,
			@RequestParam("profileImage") MultipartFile imageFile, Model model) {
		
		contactServiceImpl.updateContact(contact, user, imageFile);
		return "normal/user_dashboard";
	}
	
	/* user profile page */
	
	@GetMapping("/profile")
	public String showUserProfile(Model model) {
		
		model.addAttribute("title", modelMessage + "Profile");
		model.addAttribute("user", this.user);
		
		return "normal/user_profile";
	}
}
