package missmint.users.service;

import missmint.finance.FinanceService;
import missmint.users.model.Staff;
import missmint.users.repositories.StaffRepository;
import org.javamoney.moneta.Money;
import org.salespointframework.time.BusinessTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.money.MonetaryAmount;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@Service
public class SalaryService {
	private StaffRepository staffRepository;
	private FinanceService financeService;
	private BusinessTime time;

	@Value("${general.currency}")
	private String currency;

	public SalaryService(StaffRepository staffRepository, FinanceService financeService, BusinessTime businessTime) {
		this.staffRepository = staffRepository;
		this.financeService = financeService;
		this.time = businessTime;
	}

	public void payStaff(Duration duration) {
		LocalDate now = time.getTime().toLocalDate();
		LocalDate date = time.getTime().minus(duration).toLocalDate();

		date = date.with(TemporalAdjusters.lastDayOfMonth());
		while (date.isBefore(now)) {
			for (Staff staff : staffRepository.findAll()) {
				MonetaryAmount salary = Money.of(staff.getSalary(), currency);

				financeService.add(String.format("salary for %s on %s", staff.getUserName(), date), salary.negate());
			}

			date = date.plusMonths(1);
		}
	}
}
