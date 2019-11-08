package missmint.users.controllers;

import missmint.users.model.UserManagement;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

@Controller
public class UserController {
	private final UserManagement userManagement;

	public UserController(UserManagement userManagement) {

		Assert.notNull(userManagement, "UserManagement must not be null");

		this.userManagement = userManagement;
	}
}
