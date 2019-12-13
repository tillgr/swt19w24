package missmint.finance.service;

import missmint.finance.form.AddFinanceForm;
import org.javamoney.moneta.Money;
import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.accountancy.AccountancyEntry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.money.MonetaryAmount;

/**
 *This class is a service for Accountancy
 */
@Service
public class FinanceService {
	private final Accountancy accountancy;
	@Value("${general.currency}")
	private String currency;

	public FinanceService(Accountancy accountancy) {
		this.accountancy = accountancy;
	}

	/**
	 * creates form for adding new finance items.
	 *
	 * @param form with description and price.
	 */
	public void createFinanceItemForm(AddFinanceForm form) {
		Assert.notNull(form, "AddFinanceForm cannot be null.");

		add(form.getDescription(), Money.of(form.getPrice(), currency));
	}

	/**
	 * method for calculating the whole sum of price for all finance items
	 *
	 * @return the sum of all finance items
	 */
	public Money getSum() {
		Money sum = Money.of(0, currency);
		for (AccountancyEntry entry : accountancy.findAll()) {
			sum = sum.add(entry.getValue());
		}
		return sum;
	}

	public void add(String description, MonetaryAmount value) {
		accountancy.add(new AccountancyEntry(value, description));
	}

}
