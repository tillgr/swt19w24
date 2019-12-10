package missmint.finance;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * form for adding new finance items.
 */
public class AddFinanceForm {
	/**
	 * description of the finance item
	 *
	 * finance item must not be empty.
	 */
	@NotEmpty
	private String description;
	/**
	 * price of the material
	 *
	 * price must not be null.
	 */
	@NotNull
	@Digits(integer = Integer.MAX_VALUE, fraction = 2)
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
