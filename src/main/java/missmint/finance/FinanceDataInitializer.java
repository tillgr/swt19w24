package missmint.finance;

import org.javamoney.moneta.Money;
import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.accountancy.AccountancyEntry;
import org.salespointframework.core.DataInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(20)
public class FinanceDataInitializer implements DataInitializer {
	@Value("${general.currency}")
	private String currency;
	private Accountancy accountancy;

	public FinanceDataInitializer(Accountancy accountancy) {
		this.accountancy = accountancy;
	}

	@Override
	public void initialize() {
		accountancy.add(new AccountancyEntry(Money.of(27, currency), "200 Knöpfe kosten 400" + currency));
		accountancy.add(new AccountancyEntry(Money.of(18, currency), "30 Faden kosten 540" + currency));
	}
}
