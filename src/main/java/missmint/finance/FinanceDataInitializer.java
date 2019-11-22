package missmint.finance;

import org.javamoney.moneta.Money;
import org.salespointframework.accountancy.AccountancyEntry;
import org.salespointframework.core.DataInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.money.Monetary;

@Component
@Order(20)
public class FinanceDataInitializer implements DataInitializer {

	private static final Logger LOG = LoggerFactory.getLogger(FinanceDataInitializer.class);
	private final FinanceService financeService;

	public FinanceDataInitializer(FinanceService financeService) {
		this.financeService = financeService;
	}

	@Override
	public void initialize() {
		LOG.info("Das hat funktioniert");

		financeService.add(new AccountancyEntry(Money.of(12, "USD"), "Knöpfe x 409"));

	}
}
