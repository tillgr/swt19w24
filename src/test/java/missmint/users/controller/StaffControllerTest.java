package missmint.users.controller;

import missmint.users.forms.RegistrationForm;
import missmint.users.model.Staff;
import missmint.users.service.StaffManagement;
import org.junit.jupiter.api.Test;
import org.salespointframework.useraccount.Password;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class StaffControllerTest {

	@Autowired
	private MockMvc mvc;
	@Autowired
	private StaffManagement staffManagement;

	@Test
	void unauthenticated() throws Exception {
		mvc.perform(get("/users"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrlPattern("**/login"));
		mvc.perform(get("/users/registration"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrlPattern("**/login"));
		mvc.perform(post("/users/password/123"))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "hans", roles = {"EMPLOYEE"})
	void authenticatedAsEmployee() throws Exception {
		mvc.perform(get("/users"))
				.andExpect(status().isForbidden());
		mvc.perform(get("/users/registration"))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "Boss", roles = {"ADMIN"})
	void authenticatedAsAdmin() throws Exception {
		mvc.perform(get("/users"))
				.andExpect(status().isOk());
		mvc.perform(get("/users/registration"))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "Boss", roles = {"ADMIN"})
	void changePassword() throws Exception {
		String userName = "tolkien";
		RegistrationForm form = new RegistrationForm("John", "Tolkien", userName, "123", BigDecimal.valueOf(1));
		staffManagement.createStaff(form);
		Staff staff = staffManagement.findStaffByUserName(userName).orElseThrow();
		Password.EncryptedPassword password = staff.getUserAccount().getPassword();

		mvc.perform(post(String.format("/users/password/%d", staff.getId())).with(csrf()).param("password", "321"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/users"));

		assertThat(staff.getUserAccount().getPassword()).isNotEqualTo(password);
	}

	@Test
	@WithMockUser(username = "Boss", roles = {"ADMIN"})
	void changePasswordBlankPassword() throws Exception {
		String userName = "tolkien";
		RegistrationForm form = new RegistrationForm("John", "Tolkien", userName, "123", BigDecimal.valueOf(1));
		staffManagement.createStaff(form);
		Staff staff = staffManagement.findStaffByUserName(userName).orElseThrow();

		mvc.perform(post(String.format("/users/password/%d", staff.getId()))
				.with(csrf()).param("password", " \t ")
				.locale(Locale.ROOT)
		)
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Password cannot be empty.")));
	}

	@Test
	@WithMockUser(username = "Boss", roles = {"ADMIN"})
	void editFirstName() throws Exception {
		String userName = "tolkien";
		RegistrationForm form = new RegistrationForm("John", "Tolkien", userName, "123", BigDecimal.valueOf(1));
		staffManagement.createStaff(form);
		Staff staff = staffManagement.findStaffByUserName(userName).orElseThrow();

		mvc.perform(post(String.format("/users/%d", staff.getId()))
				.with(csrf())
				.param("firstName", "Johnny")
				.param("lastName", "Tolkien")
				.param("salary", "1")
		)
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/users"));

		assertThat(staff.getFirstName()).isEqualTo("Johnny");
	}

	@Test
	@WithMockUser(username = "Boss", roles = {"ADMIN"})
	void editFirstNameToBlank() throws Exception {
		String userName = "tolkien";
		RegistrationForm form = new RegistrationForm("John", "Tolkien", userName, "123", BigDecimal.valueOf(1));
		staffManagement.createStaff(form);
		Staff staff = staffManagement.findStaffByUserName(userName).orElseThrow();

		mvc.perform(post(String.format("/users/%d", staff.getId()))
				.with(csrf())
				.param("firstName", "  ")
				.param("lastName", "Tolkien")
				.param("salary", "1")
				.locale(Locale.ROOT)
		)
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Name must not be empty.")));
		assertThat(staff.getFirstName()).isEqualTo("John");
	}

	@Test
	@WithMockUser(username = "Boss", roles = {"ADMIN"})
	void editLastName() throws Exception {
		String userName = "tolkien";
		RegistrationForm form = new RegistrationForm("John", "Tolkien", userName, "123", BigDecimal.valueOf(1));
		staffManagement.createStaff(form);
		Staff staff = staffManagement.findStaffByUserName(userName).orElseThrow();

		mvc.perform(post(String.format("/users/%d", staff.getId()))
				.with(csrf())
				.param("firstName", "John")
				.param("lastName", "Meyer")
				.param("salary", "1")
				.locale(Locale.ROOT)
		)
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/users"));
		assertThat(staff.getLastName()).isEqualTo("Meyer");
	}

	@Test
	@WithMockUser(username = "Boss", roles = {"ADMIN"})
	void editLastNameToBlank() throws Exception {
		String userName = "tolkien";
		RegistrationForm form = new RegistrationForm("John", "Tolkien", userName, "123", BigDecimal.valueOf(1));
		staffManagement.createStaff(form);
		Staff staff = staffManagement.findStaffByUserName(userName).orElseThrow();

		mvc.perform(post(String.format("/users/%d", staff.getId()))
				.with(csrf())
				.param("firstName", "John")
				.param("lastName", "  ")
				.param("salary", "1")
				.locale(Locale.ROOT)
		)
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Name must not be empty.")));
		assertThat(staff.getLastName()).isEqualTo("Tolkien");
	}
}
