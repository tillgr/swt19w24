package missmint.finance.service;

import missmint.finance.form.AddFinanceForm;
import org.javamoney.moneta.Money;
import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.accountancy.AccountancyEntry;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.time.Interval;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.money.MonetaryAmount;
import java.time.LocalDateTime;

/**
 *This class is a service for Accountancy
 */
@Service
public class FinanceService {
	private final Accountancy accountancy;
	private BusinessTime time;
	@Value("${general.currency}")
	private String currency;


	public FinanceService(Accountancy accountancy, BusinessTime businessTime) {
		this.accountancy = accountancy;
		this.time = businessTime;
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
	public Money getSum(Streamable<AccountancyEntry> entries) {
		Money sum = Money.of(0, currency);
		for (AccountancyEntry entry : entries) {
			sum = sum.add(entry.getValue());
		}
		return sum;
	}

	/**
	 * Adds a accountancy item to the database if the value is not zero.
	 *
	 * @param description A user-supplied description for this entry.
	 * @param value The value that is stored in the database.
	 */
	public void add(String description, MonetaryAmount value) {
		if (value.isZero()) {
			return;
		}

		accountancy.add(new AccountancyEntry(value, description));
	}

	public Streamable<AccountancyEntry> lastMonth() {
		LocalDateTime lastMonth = time.getTime().minusMonths(1);
		LocalDateTime firstDay = lastMonth.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
		LocalDateTime lastDay = time.getTime().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);

		return accountancy.find(Interval.from(firstDay).to(lastDay));
	}

}
