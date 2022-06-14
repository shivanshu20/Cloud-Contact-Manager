package com.contactmanager.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.contactmanager.helper.Message;
import com.contactmanager.model.User;
import com.contactmanager.service.implementations.UserServiceImpl;

@Controller
@RequestMapping("/cloudcontactmanager")
public class HomeController {

	private final String modelMessage = "Cloud Contact Manager";
	private final UserServiceImpl userService;

	@Autowired
	public HomeController(UserServiceImpl userService) {

		this.userService = userService;
	}

	/* home page */

	@RequestMapping("/")
	public String home(Model model) {

		model.addAttribute("title", "Home - " + modelMessage);
		return "home";
	}

	/* about page */

	@RequestMapping("/about")
	public String about(Model model) {

		model.addAttribute("title", "About - " + modelMessage);
		return "about";
	}

	/* sign-up form page */

	@RequestMapping("/signup")
	public String signUpForm(Model model) {

		User user = new User();

		model.addAttribute("title", "Register - " + modelMessage);
		model.addAttribute("user", user);
		return "signup";
	}

	/* sign in form page */

	@RequestMapping("/signin")
	public String signInForm(Model model) {

		model.addAttribute("title", "Login - " + modelMessage);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
			return "signin";

		return "redirect:/cloudcontactmanager/user/dashboard";
	}

	/* register-user */

	@PostMapping("/register-user")
	public String registerUser(@Valid @ModelAttribute("user") User user, Model model) {

		User savedUser = userService.registerUser(user);

		if (savedUser != null) {
			
			model.addAttribute("message", new Message("Successfully registered", "alert-success"));
			model.addAttribute("user", new User());
		}

		else {
			model.addAttribute("messsage", new Message("Something went wrong!", "alert-danger"));
		}

		return "signup";
	}

}
