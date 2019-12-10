package missmint.finance;

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
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalAmount;

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

	public void createFinanceItemForm(AddFinanceForm form) {
		Assert.notNull(form, "AddFinanceForm cannot be null.");

		add(form.getDescription(), Money.of(form.getPrice(), currency));
	}

	public Money getSum(Streamable<AccountancyEntry> entries) {
		Money sum = Money.of(0, currency);
		for (AccountancyEntry entry : entries) {
			sum = sum.add(entry.getValue());
		}
		return sum;
	}

	public void add(String description, MonetaryAmount value) {
		accountancy.add(new AccountancyEntry(value, description));
	}

	public Streamable<AccountancyEntry> lastMonth() {
		LocalDateTime lastMonth = time.getTime().minusMonths(1);
		LocalDateTime firstDay = lastMonth.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
		LocalDateTime lastDay = time.getTime().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);

		return accountancy.find(Interval.from(firstDay).to(lastDay));
	}

}
