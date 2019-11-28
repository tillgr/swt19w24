package missmint.finance;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class AddFinanceForm {
	@NotEmpty
	private String description;

	@NotNull
	@Digits(integer = Integer.MAX_VALUE, fraction = 2)
	@Min(value = 0)
	private BigDecimal price;

	public AddFinanceForm(BigDecimal price, String description) {
		this.price = price;
		this.description = description;
	}

	public BigDecimal getPrice() {
		return this.price;
	}

	public String getDescription() {
		return this.description;
	}

}
