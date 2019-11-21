package missmint.finance;

import org.salespointframework.accountancy.AccountancyEntry;
//import org.salespointframework.accountancy.AccountancyEntry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinanceRepository extends CrudRepository<AccountancyEntry, Long> {
}
