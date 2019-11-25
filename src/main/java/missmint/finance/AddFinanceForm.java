package missmint.finance;

import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Description;

import javax.money.MonetaryAmount;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
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
