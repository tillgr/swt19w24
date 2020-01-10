package missmint.users.controller;

import missmint.Utils;
import missmint.orders.service.ServiceCategory;
import missmint.time.TimeTableService;
import missmint.users.forms.EditStaffForm;
import missmint.users.forms.PasswordForm;
import missmint.users.forms.RegistrationForm;
import missmint.users.model.Staff;
import missmint.users.repositories.StaffRepository;
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

@Controller
public class StaffController {

	private final StaffManagement staffManagement;
	private final StaffRepository staffRepository;
	private final TimeTableService timeTableService;

	public StaffController(StaffManagement staffManagement,
						   StaffRepository staffRepository,
						   TimeTableService timeTableService
	) {
		Assert.notNull(staffManagement, "StaffManagement must not be null");

		this.staffRepository = staffRepository;
		this.staffManagement = staffManagement;
		this.timeTableService = timeTableService;
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
	@PostMapping("/users/delete/{userName}")
	public String deleteUser(@PathVariable String userName) {

		staffManagement.deleteStaff(userName);
		timeTableService.rebuildTimeTable();

		return "redirect:/users";
	}

	/**
	 * Show the registration form to add new users
	 *
	 * @return the registration view
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/users/registration")
	public String registrationForm(@ModelAttribute("form") RegistrationForm form) {
		return "registration";
	}

	/**
	 * Uses the registration form to create a new staff with user account
	 *
	 * @param form   used for creating account
	 * @param result errors in form
	 * @return view
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/users/registration")
	public String registerStaff(@Valid @ModelAttribute("form") RegistrationForm form, Errors result) {

		if (result.hasErrors()) {
			return registrationForm(form);
		}

		if (staffManagement.findByUserName(form.getUserName()).isPresent()) {
			result.rejectValue(
				"userName",
				"RegistrationForm.username.duplicate",
				"An account with this name already exists."
			);
			return registrationForm(form);
		}

		staffManagement.createStaff(form);

		return "redirect:/users";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("users/edit/{id}")
	public String editUserPage(@PathVariable("id") long id,
							   @ModelAttribute("editform") EditStaffForm editForm,
							   @ModelAttribute("pwdform") PasswordForm pwdForm,
							   Model model) {
		var staff = Utils.getOrThrow(staffRepository.findById(id));
		model.addAttribute("staff", staff);

		model.addAttribute("services", new HashSet<>(EnumSet.allOf(ServiceCategory.class)));

		return "edituser";
	}

	@PreAuthorize(("hasRole('ADMIN')"))
	@PostMapping("/users/{id}")
	public String saveUser(
		@PathVariable long id,
		@ModelAttribute("pwdform") PasswordForm pwdForm,
		@Valid @ModelAttribute("editform") EditStaffForm editForm,
		Errors result,
		Model model
	) {
		Staff staff = Utils.getOrThrow(staffRepository.findById(id));

		if (result.hasErrors()) {
			return editUserPage(id, editForm, pwdForm, model);
		}

		staffManagement.editStaff(staff, editForm.getFirstName(), editForm.getLastName(), editForm.getSalary(), editForm.getUpdateSkills());

		timeTableService.rebuildTimeTable();

		return "redirect:/users";
	}

	/**
	 * Update the password of a staff member.
	 *
	 * @param id id of the staff member
	 * @param pwdForm the form with the new password
	 * @param result errors in the form
	 * @return a redirect to the users page or the edit page again on error
	 * @see StaffController#editUserPage(long, EditStaffForm, PasswordForm, Model)
	 */
	@PreAuthorize(("hasRole('ADMIN')"))
	@PostMapping("/users/password/{id}")
	public String changePassword(
		@PathVariable long id,
		@ModelAttribute("editform") EditStaffForm editForm,
		@Valid @ModelAttribute("pwdform") PasswordForm pwdForm,
		Errors result,
		Model model
	) {
		Staff staff = Utils.getOrThrow(staffRepository.findById(id));

		if (result.hasErrors()) {
			return editUserPage(id, editForm, pwdForm, model);
		}

		staffManagement.changePassword(staff, pwdForm.getPassword());

		return "redirect:/users";
	}
}
