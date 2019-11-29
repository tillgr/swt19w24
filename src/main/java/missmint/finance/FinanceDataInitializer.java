package missmint.finance;

import org.javamoney.moneta.Money;
import org.salespointframework.core.DataInitializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(20)
public class FinanceDataInitializer implements DataInitializer {
	private final FinanceService financeService;
	@Value("${general.currency}")
	private String currency;

	public FinanceDataInitializer(FinanceService financeService) {
		this.financeService = financeService;
	}

	@Override
	public void initialize() {
		financeService.add("200 Knöpfe kosten 400" + currency, Money.of(27, currency));
		financeService.add("30 Faden kosten 540" + currency, Money.of(18, currency));
	}
}
