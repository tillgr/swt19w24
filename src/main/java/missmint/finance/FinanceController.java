package missmint.finance;

import org.javamoney.moneta.Money;
import org.salespointframework.accountancy.AccountancyEntry;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;

import javax.money.MonetaryAmount;
import java.util.ArrayList;

@Controller
public class FinanceController {

    private FinanceService financeService;
	ArrayList <MonetaryAmount> ma = new ArrayList<>();
	Money sum = Money.of(0, "USD");
	FinanceController(FinanceService financeService){
		Assert.notNull(financeService, "Finance Manager must not be Null");
		this.financeService = financeService;
	}

	@GetMapping("/finance")
	public String showFinancePage(Model model)
	{
		model.addAttribute("finance" , financeService.showAllFinance());
		model.addAttribute("sum", getSum());
		return "finance";
	}

	public ArrayList<MonetaryAmount> getValue(){
		for (AccountancyEntry a: financeService.showAllFinance()){
			ma.add(a.getValue());
		}
		return  ma;
	}

	public Money getSum(){
		for (MonetaryAmount monetaryAmount : getValue()) {
			sum = sum.add(monetaryAmount);
		}
		return  sum;
	}
}
