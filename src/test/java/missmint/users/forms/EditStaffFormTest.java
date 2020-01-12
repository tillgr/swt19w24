package missmint.users.forms;

import missmint.orders.service.ServiceCategory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.Set;

public class EditStaffFormTest {

	private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	@Test
	void validForm() {
		var form = new EditStaffForm("Max", "Muster", Set.of(ServiceCategory.GRINDERY), BigDecimal.valueOf(20.1));
		var violations = validator.validate(form);
		Assertions.assertTrue(violations.isEmpty());
	}

	@Test
	void blankFirstName() {
		var form = new EditStaffForm("", "Muster", Set.of(ServiceCategory.GRINDERY), BigDecimal.valueOf(20.1));
		var violations = validator.validate(form);
		Assertions.assertEquals(1, violations.size());
	}

	@Test
	void blankLastName() {
		var form = new EditStaffForm("asd", " ", Set.of(ServiceCategory.GRINDERY), BigDecimal.valueOf(20.1));
		var violations = validator.validate(form);
		Assertions.assertEquals(1, violations.size());
	}

	@Test
	void negativeSalary() {
		var form = new EditStaffForm("asd", "w", Set.of(ServiceCategory.KLUDGE), BigDecimal.valueOf(-0.1));
		var violations = validator.validate(form);
		Assertions.assertEquals(1, violations.size());
	}

	@Test
	void tooManyDecimals() {
		var form = new EditStaffForm("asd", "w", Set.of(ServiceCategory.KLUDGE), BigDecimal.valueOf(10.001));
		var violations = validator.validate(form);
		Assertions.assertEquals(1, violations.size());
	}
}
