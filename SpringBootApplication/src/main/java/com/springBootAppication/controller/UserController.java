package com.springBootAppication.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.springBootAppication.dto.ChangePasswordForm;
import com.springBootAppication.entity.Role;
import com.springBootAppication.entity.User;
import com.springBootAppication.exception.CustomFieldValidationException;
import com.springBootAppication.exception.UsernmeOrIdNotFoundException;
import com.springBootAppication.repository.RoleRepository;
import com.springBootAppication.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	RoleRepository roleRepository;

	
	// Los métodos con GET nos sirven para obtener datos de la vista.
	// Los métodos con POST nos sirven para enviar datos a la vista.
	
	@GetMapping({"/", "/login"})
	public String index() {
		return "index";	// Buscará automáticamente en la carpeta 'templates' un archivo que se llame 'index.html'.
	}
	
	@GetMapping("/userForm")
	public String userForm(Model model) {
		model.addAttribute("userForm", new User());
		model.addAttribute("userList", userService.getAllUsers());
		model.addAttribute("roles", roleRepository.findAll());
		model.addAttribute("listTab", "active");
		
		return "user-form/user-view";
	}
	
	@GetMapping("/signup")
	public String signup(Model model) {
		Role userRole = roleRepository.findByName("USER");
		List<Role> roles = Arrays.asList(userRole);
		model.addAttribute("userForm", new User());
		model.addAttribute("roles", roles);
		model.addAttribute("signup", true);
		
		return("user-form/user-signup");
	}
	
	@PostMapping("/signup")
	public String postSignup(@Valid @ModelAttribute("userForm") User user, BindingResult result, ModelMap model) {
		Role userRole = roleRepository.findByName("USER");
		List<Role> roles = Arrays.asList(userRole);
		model.addAttribute("userForm", user);
		model.addAttribute("roles", roles);
		model.addAttribute("signup", true);
		
		// Verificamos si el resultado tiene errores:
		if (result.hasErrors()) {
			return("user-form/user-signup");
		} else { // Si no hay errores, procedemos a crear el usuario introducido en la vista:
			try {
				userService.createUser(user);
			} catch (CustomFieldValidationException cfve) {
				result.rejectValue(cfve.getFieldName(), null, cfve.getMessage());
				return("user-form/user-signup");
			} catch (Exception e) {
				model.addAttribute("formErrorMessage", e.getMessage());
				return("user-form/user-signup");
			}
		}

		model.addAttribute("userList", userService.getAllUsers());
		model.addAttribute("roles", roleRepository.findAll());
		
		return "index";
	}
	
	@PostMapping("/userForm")	// El BindingResult encapsula los errores producidos al realizar la validación.
	public String createUser(@Valid @ModelAttribute("userForm") User user, BindingResult result, ModelMap model) {
		// Verificamos si el resultado tiene errores:
		if (result.hasErrors()) {
			model.addAttribute("userForm", user);	// Devolvemos el usuario recibido para que no se pierdan los datos que se introdujeron en el formulario.
			model.addAttribute("formTab", "active");
		} else { // Si no hay errores, procedemos a crear el usuario introducido en la vista:
			try {
				userService.createUser(user);
				model.addAttribute("userForm", new User());
				model.addAttribute("listTab", "active");
			} catch (CustomFieldValidationException cfve) {
				result.rejectValue(cfve.getFieldName(), null, cfve.getMessage());
				model.addAttribute("userForm", user);
				model.addAttribute("formTab", "active");
				model.addAttribute("userList", userService.getAllUsers());
				model.addAttribute("roles", roleRepository.findAll());
				
			} catch (Exception e) {
				model.addAttribute("formErrorMessage", e.getMessage());
				model.addAttribute("userForm", user);
				model.addAttribute("formTab", "active");
				model.addAttribute("userList", userService.getAllUsers());
				model.addAttribute("roles", roleRepository.findAll());
				
				e.printStackTrace();
			}
		}

		model.addAttribute("userList", userService.getAllUsers());
		model.addAttribute("roles", roleRepository.findAll());
		
		return "user-form/user-view";
	}
	
	@GetMapping("/editUser/{id}") // Indicamos al Spring MVC que vamos a recibir un parámetro llamado 'id'.
	public String getEditUserForm(Model model, @PathVariable(name="id") Long id) throws Exception {
		User userToEdit = userService.getUserById(id);
		
		model.addAttribute("userForm", userToEdit);
		model.addAttribute("userList", userService.getAllUsers());
		model.addAttribute("roles", roleRepository.findAll());
		model.addAttribute("formTab", "active");
		model.addAttribute("editMode", "true");
		model.addAttribute("passwordForm", new ChangePasswordForm(id));
		
		return "user-form/user-view";
	}
	
	@PostMapping("/editUser")	// El BindingResult encapsula los errores producidos al realizar la validación.
	public String postEditUserForm(@Valid @ModelAttribute("userForm") User user, BindingResult result, ModelMap model) {
		// Verificamos si el resultado tiene errores:
		if (result.hasErrors()) {
			model.addAttribute("userForm", user);	// Devolvemos el usuario recibido para que no se pierdan los datos que se introdujeron en el formulario.
			model.addAttribute("formTab", "active");
			model.addAttribute("editMode", "true");
			model.addAttribute("passwordForm", new ChangePasswordForm(user.getId()));
		} else { // Si no hay errores, procedemos a crear el usuario introducido en la vista:
			try {
				userService.updateUser(user);
				model.addAttribute("userForm", new User());
				model.addAttribute("listTab", "active");
			} catch (Exception e) {
				model.addAttribute("formErrorMessage", e.getMessage());
				model.addAttribute("userForm", user);
				model.addAttribute("formTab", "active");
				model.addAttribute("userList", userService.getAllUsers());
				model.addAttribute("roles", roleRepository.findAll());
				model.addAttribute("editMode", "true");
				model.addAttribute("passwordForm", new ChangePasswordForm(user.getId()));
			}
		}

		model.addAttribute("userList", userService.getAllUsers());
		model.addAttribute("roles", roleRepository.findAll());
		
		return "user-form/user-view";
	}
	
	@GetMapping("/userForm/cancel")
	public String calcelEditUser(ModelMap model) {
		return "redirect:/userForm";
	}

	@GetMapping("/deleteUser/{id}")
	public String deleteUser(Model model, @PathVariable(name="id") Long id) {
		try {
			userService.deleteUser(id);
		} catch (UsernmeOrIdNotFoundException uoinfe) {
			model.addAttribute("listErrorMessage", uoinfe.getMessage());
		}
		
		return userForm(model);
	}
	
	@PostMapping("/editUser/changePassword")
	public ResponseEntity<String> postEditUserChangePassword(@Valid @RequestBody ChangePasswordForm form, Errors errores) {
		try {
			if (errores.hasErrors()) {
				String result = errores.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(""));
				throw new Exception(result);
			}
			
			userService.changePassword(form);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
		return ResponseEntity.ok("Success");
	}
}
