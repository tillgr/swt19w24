package missmint.finance;

import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;
import javax.validation.constraints.NotEmpty;

public class AddFinanceForm {

	@NotEmpty
	private String description;

	private MonetaryAmount price;

	public AddFinanceForm(MonetaryAmount price, String description){
		this.price=price;
		this.description=description;
	}

	public MonetaryAmount getPrice(){
		return this.price;
	}

	public String getDescription(){
		return this.description;
	}

}
