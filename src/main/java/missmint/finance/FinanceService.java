package missmint.finance;

import org.javamoney.moneta.Money;
import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.accountancy.AccountancyEntry;
import org.springframework.stereotype.Service;

import javax.money.MonetaryAmount;
import java.util.ArrayList;
import java.util.Map;

@Service
public class FinanceService {
	private final Accountancy accountancy;



	public FinanceService(Accountancy accountancy) {
		this.accountancy = accountancy;
	}

	public Iterable <AccountancyEntry> showAllFinance(){
		return accountancy.findAll();
	}


	public void add(AccountancyEntry accountancyEntry){
		accountancy.add(accountancyEntry);
	}

}
