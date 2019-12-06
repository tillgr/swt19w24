package missmint.orders.forms;

import missmint.orders.service.MissMintService;
import org.assertj.core.api.Assertions;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.salespointframework.catalog.ProductIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.Validator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.util.Set;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootTest
public class ReceivingFormTests {
	@Autowired
	Validator validator;

	@Test
	void valid() {
		ReceivingForm form = new ReceivingForm("Caesar", "Brutus", getProductIdentifier());
		Set<ConstraintViolation<ReceivingForm>> violations = Validation.buildDefaultValidatorFactory().getValidator().validate(form);
		Assertions.assertThat(violations.size()).isZero();
	}

	@Test
	void blankCustomer() {
		ReceivingForm form = new ReceivingForm("\t", "Brutus", getProductIdentifier());
		Set<ConstraintViolation<ReceivingForm>> violations = Validation.buildDefaultValidatorFactory().getValidator().validate(form);
		Assertions.assertThat(violations.size()).isPositive();
	}

	@Test
	void blankDescription() {
		ReceivingForm form = new ReceivingForm("Caesar", " ", getProductIdentifier());
		Set<ConstraintViolation<ReceivingForm>> violations = Validation.buildDefaultValidatorFactory().getValidator().validate(form);
		Assertions.assertThat(violations.size()).isPositive();
	}

	@Test
	void nullService() {
		ReceivingForm form = new ReceivingForm("Caesar", "Brutus", null);
		Set<ConstraintViolation<ReceivingForm>> violations = Validation.buildDefaultValidatorFactory().getValidator().validate(form);
		Assertions.assertThat(violations.size()).isPositive();
	}

	private ProductIdentifier getProductIdentifier() {
		MissMintService service = new MissMintService("service", Money.of(0, "EUR"));
		return service.getId();
	}
}
