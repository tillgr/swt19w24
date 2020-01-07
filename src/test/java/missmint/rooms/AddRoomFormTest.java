package missmint.rooms;

import missmint.orders.forms.ReceivingForm;
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
public class AddRoomFormTest {
	@Autowired
	Validator validator;

	@Test
	void blankName(){
		AddRoomForm form = new AddRoomForm("");
		Set<ConstraintViolation<AddRoomForm>> violations = Validation.buildDefaultValidatorFactory().getValidator().validate(form);
		Assertions.assertThat(violations.size()).isPositive();
	}



}
