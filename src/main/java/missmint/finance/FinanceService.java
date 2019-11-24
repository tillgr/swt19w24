package missmint.finance;

import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.accountancy.AccountancyEntry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

	public void getDate(){
		for(AccountancyEntry a: showAllFinance()){
			a.getDate();
		}
	}

}
