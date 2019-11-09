package com.springBootAppication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

	@GetMapping("/")
	public String index() {
		return "index";	// Buscará automáticamente en la carpeta 'templates' un archivo que se llame 'index.html'.
	}
	
	@GetMapping("/userForm")
	public String userForm() {
		return "user-form/user-view";
	}
}
