package missmint.finance;

import org.javamoney.moneta.Money;
import org.salespointframework.accountancy.AccountancyEntry;
import org.salespointframework.core.DataInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(20)
public class FinanceDataInitializer implements DataInitializer {

	private static final Logger LOG = LoggerFactory.getLogger(FinanceDataInitializer.class);
	private FinanceService financeService;



	public FinanceDataInitializer(FinanceService financeService) {
		this.financeService = financeService;
		}

	@Override
	public void initialize() {
		financeService.add(new AccountancyEntry(Money.of(27, "USD"), "200 Knöpfe kosten 400 USD"  ));
		financeService.add(new AccountancyEntry(Money.of(18, "USD"), "30 Faden kosten 540 USD" ));
	}
}
