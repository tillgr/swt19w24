package missmint.finance;

import org.javamoney.moneta.Money;
import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.accountancy.AccountancyEntry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class FinanceService {
	private final Accountancy accountancy;
	@Value("${general.currency}")
	private String currency;

	public FinanceService(Accountancy accountancy) {
		this.accountancy = accountancy;
	}

	public Iterable <AccountancyEntry> showAllFinance(){
		return accountancy.findAll();
	}


	public void add(AccountancyEntry accountancyEntry){
		accountancy.add(accountancyEntry);
	}


	public  AccountancyEntry createFinanceItemForm(AddFinanceForm form){
		Assert.notNull(form, "AddFinanceForm cannot be null.");
		String sPrice = form.getPrice();

		//todo

		return accountancy.add(new AccountancyEntry(Money.of(0, currency), form.getDescription()));
	}

	public Money getSum(){
		Money sum = Money.of(0, currency);
		for (AccountancyEntry entry : showAllFinance()) {
			sum = sum.add(entry.getValue());
		}
		return  sum;
	}

}
