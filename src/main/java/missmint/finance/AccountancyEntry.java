package missmint.finance;

import org.salespointframework.accountancy.AccountancyEntryIdentifier;
import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.core.AbstractEntity;
import org.salespointframework.time.Interval;
import org.springframework.data.util.Streamable;

import javax.money.MonetaryAmount;
import java.time.temporal.TemporalAmount;
import java.util.Map;
import java.util.Optional;

public class AccountancyEntry extends AbstractEntity<AccountancyEntryIdentifier> implements Accountancy {
	private AccountancyEntryIdentifier id;


	public AccountancyEntryIdentifier getId() {
		return this.id;
	}



	public <T extends org.salespointframework.accountancy.AccountancyEntry> T add(T accountancyEntry) {
		return this.add(accountancyEntry);
	}

	//public FinanceItem  addFinanceItem(FinanceItem financeItem){
	//	return financeRepository.save(financeItem);
	//}

		public Streamable<org.salespointframework.accountancy.AccountancyEntry> findAll() {
		return null;
	}


	public Optional<org.salespointframework.accountancy.AccountancyEntry> get(AccountancyEntryIdentifier accountancyEntryIdentifier) {
		return Optional.empty();
	}


	public Streamable<org.salespointframework.accountancy.AccountancyEntry> find(Interval interval) {
		return null;
	}


	public Map<Interval, Streamable<org.salespointframework.accountancy.AccountancyEntry>> find(Interval interval, TemporalAmount duration) {
		return null;
	}


	public Map<Interval, MonetaryAmount> salesVolume(Interval interval, TemporalAmount duration) {
		return null;
	}
}
