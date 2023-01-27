package com.midlaj.springCRUD.controller;

import com.midlaj.springCRUD.model.User;
import com.midlaj.springCRUD.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AppController {

	@Autowired
	UserService userService;

	
	@RequestMapping("/")
	public String viewHomePage(Model model) {
		List<User> listUser = userService.listAll();
		model.addAttribute("listUser",listUser);
		return "index";
	}


	@RequestMapping("/login")
	public String viewLoginPage(){
		System.out.println("Hi");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || authentication instanceof AnonymousAuthenticationToken){
			return "login";
		}
		else {
			return "redirect:/";
		}
	}

	@RequestMapping("/edit/{id}")
	public String showEditUserPage(Model model, @PathVariable (name="id") Long id) {
//		ModelAndView mav=new ModelAndView("edit_student");
//		User user=userService.get(id);
//		mav.addObject("user",user);
		User user=new User();
		model.addAttribute(user);
		model.addAttribute(id);
		return "edit_student";
	}

	@PostMapping(path="/edit/{userId}")
	public String updateStudent(
			@PathVariable("userId") Long userId,
			@RequestParam(required = false) String firstName,
			@RequestParam(required = false) String lastName,
			@RequestParam(required = false) String username,
			@RequestParam(required = false) String password)
	{
		userService.updateUser(userId,username,firstName,lastName,password);

		return "redirect:/";
	}
	
	@RequestMapping("/delete/{id}")
	public String deleteUserPage(@PathVariable (name="id") Long id) {
		userService.delete(id);
		return "redirect:/";
	}

	@RequestMapping("/register")
	public String showRegistrationForm(Model model) {
		User user=new User();
		model.addAttribute(user);
		return "register";
	}

	@RequestMapping(value = "/register/saveUser",method = RequestMethod.POST)
	public String saveUser(@ModelAttribute("user") User user){
		userService.registerDefaultUser(user);
		return ("redirect:/login");
	}
}
