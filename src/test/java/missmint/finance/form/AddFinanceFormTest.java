package missmint.finance.form;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import java.math.BigDecimal;

class AddFinanceFormTest {
	private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	@Test
	void validForm() {
		var form = new AddFinanceForm(BigDecimal.valueOf(10.8), "10 Knöpfe kosten 300 EUR");
		var violations = validator.validate(form);
		Assertions.assertTrue(violations.isEmpty());
	}

	@Test
	void emptyDescription() {
		var form = new AddFinanceForm(BigDecimal.valueOf(10.8) , "");
		var violations = validator.validate(form);
		Assertions.assertEquals(1, violations.size());
	}

	@Test
	void tooManyDecimals() {
		var form = new AddFinanceForm(BigDecimal.valueOf(10.001) , "");
		var violations = validator.validate(form);
		Assertions.assertEquals(1, violations.size());
	}

	@Test
	void notNullPrice() {
		var form = new AddFinanceForm(null, "10 Knöpfe kosten 300 EUR");
		var violations = validator.validate(form);
		Assertions.assertTrue(violations.isEmpty());
	}
}