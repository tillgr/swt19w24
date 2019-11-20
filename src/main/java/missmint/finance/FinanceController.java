package missmint.finance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FinanceController {

    private AccountancyEntry accountancyEntry;

	FinanceController() {
	}

	FinanceController(AccountancyEntry accountancyEntry){
		Assert.notNull(accountancyEntry, "Finance Manager must not be Null");
		this.accountancyEntry = accountancyEntry;
	}

	@GetMapping("/finance")
	public String showFinancePage(Model model)
	{
		model.addAttribute("finance" , accountancyEntry.findAll());
		return "finance";
	}
}
