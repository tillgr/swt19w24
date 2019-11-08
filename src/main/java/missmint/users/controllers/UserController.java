package missmint.users.controllers;

import missmint.users.model.UserManagement;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
	private final UserManagement userManagement;

	public UserController(UserManagement userManagement) {

		Assert.notNull(userManagement, "UserManagement must not be null");

		this.userManagement = userManagement;
	}

	/**
	 * Populates the users page
	 *
	 * @param model injected by spring
	 * @return users view
	 */
	@GetMapping("/users")
	public String listUsers(Model model) {
		model.addAttribute("users", userManagement.getAllUsers());
		return "users";
	}
}
