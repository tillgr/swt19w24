package missmint.users.forms;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import java.math.BigDecimal;

public class RegistrationFormTest {

	private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	@Test
	void validForm() {
		var form = new RegistrationForm("Jacob", "Smith", "XXX", "1234", BigDecimal.valueOf(10));
		var violations = validator.validate(form);
		Assertions.assertTrue(violations.isEmpty());
	}

	@Test
	void blankFirstName() {
		var form = new RegistrationForm(" ", "Smith", "XXX", "1234", BigDecimal.valueOf(10));
		var violations = validator.validate(form);
		Assertions.assertEquals(1, violations.size());
	}

	@Test
	void blankLastName() {
		var form = new RegistrationForm("Jacob", " ", "XXX", "1234", BigDecimal.valueOf(10));
		var violations = validator.validate(form);
		Assertions.assertEquals(1, violations.size());
	}

	@Test
	void blankUsername() {
		var form = new RegistrationForm("Jacob", "Smith", "   ", "1234", BigDecimal.valueOf(10));
		var violations = validator.validate(form);
		Assertions.assertEquals(1, violations.size());
	}

	@Test
	void emptyPassword() {
		var form = new RegistrationForm("Jacob", "Smith", "xxx", "", BigDecimal.valueOf(10));
		var violations = validator.validate(form);
		Assertions.assertEquals(1, violations.size());
	}

	@Test
	void negativeSalary() {
		var form = new RegistrationForm("Jacob", "Smith", "xxx", "12", BigDecimal.valueOf(-10));
		var violations = validator.validate(form);
		Assertions.assertEquals(1, violations.size());
	}

	@Test
	void tooManyDecimals() {
		var form = new RegistrationForm("Jacob", "Smith", "xxx", "12", BigDecimal.valueOf(21.004));
		var violations = validator.validate(form);
		Assertions.assertEquals(1, violations.size());
	}
}
