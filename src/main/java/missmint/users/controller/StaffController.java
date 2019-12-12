package missmint.users.controller;

import missmint.Utils;
import missmint.orders.service.ServiceCategory;
import missmint.time.TimeTableService;
import missmint.users.forms.EditStaffForm;
import missmint.users.forms.RegistrationForm;
import missmint.users.model.AccountRole;
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
	public String registrationForm(Model model, @ModelAttribute("form") RegistrationForm form) {
		model.addAttribute("roles", new HashSet<>(EnumSet.allOf(AccountRole.class)));

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
	public String registerStaff(@Valid @ModelAttribute("form") RegistrationForm form, Errors result, Model model) {
		if (result.hasErrors() || staffManagement.findByUserName(form.getUserName()).isPresent()) {
			return registrationForm(model, form);
		}

		staffManagement.createStaff(form);

		return "redirect:/users";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("users/edit/{id}")
	public String editUserPage(@PathVariable("id") long id, EditStaffForm form , Model model) {
		var staff = Utils.getOrThrow(staffRepository.findById(id));
		model.addAttribute("staff", staff);

		model.addAttribute("services", new HashSet<>(EnumSet.allOf(ServiceCategory.class)));

		model.addAttribute("form", form);

		return "edituser";
	}

	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	@PreAuthorize(("hasRole('ADMIN')"))
	@PostMapping("/users/{id}")
	public String saveUser(
		@PathVariable long id,
		@Valid @ModelAttribute("form") EditStaffForm form,
		Errors result,
		Model model
	) {
		Staff staff = Utils.getOrThrow(staffRepository.findById(id));

		if (result.hasErrors()) {
			return editUserPage(id, form, model);
		}

		staffManagement.editStaff(staff, form.getFirstName(), form.getLastName(), form.getSalary(), form.getNewSkill());

		return "redirect:/users";
	}
}
