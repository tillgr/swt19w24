package missmint.finance;

import org.javamoney.moneta.Money;
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

	//private static final Logger LOG = LoggerFactory.getLogger(FinanceDataInitializer.class);
	private FinanceService financeService;

	@Value("${general.currency}")
	private String currency;

	public FinanceDataInitializer(FinanceService financeService) {
		this.financeService = financeService;
		}

	@Override
	public void initialize() {
		financeService.add(new AccountancyEntry(Money.of(27, currency), "200 Knöpfe kosten 400" + currency  ));
		financeService.add(new AccountancyEntry(Money.of(18, currency), "30 Faden kosten 540" + currency ));
	}
}
