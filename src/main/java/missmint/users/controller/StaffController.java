package missmint.users.controller;

import missmint.orders.service.ServiceCategory;
import missmint.users.forms.EditStaffForm;
import missmint.users.forms.RegistrationForm;
import missmint.users.model.AccountRole;
import missmint.users.service.StaffManagement;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.NoSuchElementException;

@Controller
public class StaffController {

	private final StaffManagement staffManagement;

	public StaffController(StaffManagement staffManagement) {

		Assert.notNull(staffManagement, "StaffManagement must not be null");

		this.staffManagement = staffManagement;
	}

	/**
	 * Populates the users page
	 *
	 * @param model injected by spring
	 * @return users view
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/users")
	public String listUsers(Model model) {
		model.addAttribute("users", staffManagement.getAllStaff());
		return "users";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/users/delete/{userName}/{id}")
	public String deleteUser(@PathVariable String userName, @PathVariable Long id) {

		staffManagement.deleteStaff(userName, id);

		return "redirect:/users";
	}

	/**
	 * Show the registration form to add new users
	 *
	 * @return the registration view
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/users/registration")
	public String registrationForm(Model model, RegistrationForm form) {

		model.addAttribute("form", form);
		model.addAttribute("roles", new HashSet<>(EnumSet.allOf(AccountRole.class)));

		return "registration";
	}

	/**
	 *
	 * Uses the registration form to create a new staff with user account
	 *
	 * @param form used for creating account
	 * @param result errors in form
	 * @return view
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/users/registration")
	public String registerStaff(@Valid @ModelAttribute("form") RegistrationForm form, Errors result, Model model) {

		if (result.hasErrors() ||
			staffManagement.findByUserName(form.getUserName()).isPresent()
		) {
			return registrationForm(model, form);
		}

		staffManagement.createStaff(form);

		return "redirect:/users";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("users/edit/{id}")
	public String editUserPage(@PathVariable Long id, EditStaffForm form , Model model) {

		var staff = staffManagement.findStaffById(id).orElseThrow(NoSuchElementException::new);

		model.addAttribute("staff", staff);

		model.addAttribute("services", new HashSet<>(EnumSet.allOf(ServiceCategory.class)));

		model.addAttribute("form", form);

		return "edituser";
	}

	@PreAuthorize(("hasRole('ADMIN')"))
	@PostMapping("/users/{id}")
	public String saveUser(
		@PathVariable Long id,
		@Valid @ModelAttribute("form") EditStaffForm form,
		Errors result,
		Model model
	) {

		if (result.hasErrors()) {
			return editUserPage(id, form, model);
		}

		staffManagement.editStaff(id, form.getFirstName(), form.getLastName(), form.getNewSkill());

		return "redirect:/users";
	}
}
