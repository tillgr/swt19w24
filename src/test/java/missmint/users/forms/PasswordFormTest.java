package missmint.users.forms;

import missmint.orders.forms.ReceivingForm;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.Validator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.util.Set;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootTest
public class PasswordFormTest {
	@Autowired
	Validator validator;

	@Test
	void valid() {
		PasswordForm form = new PasswordForm("securepass2000");
		Set<ConstraintViolation<PasswordForm>> violations = Validation.buildDefaultValidatorFactory().getValidator().validate(form);
		Assertions.assertThat(violations.size()).isZero();
	}

	@Test
	void blank() {
		PasswordForm form = new PasswordForm("\t   ");
		Set<ConstraintViolation<PasswordForm>> violations = Validation.buildDefaultValidatorFactory().getValidator().validate(form);
		Assertions.assertThat(violations.size()).isPositive();
	}
}
