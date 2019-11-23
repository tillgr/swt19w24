package missmint.finance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FinanceController {

    private FinanceService financeService;

	FinanceController(FinanceService financeService){
		Assert.notNull(financeService, "Finance Manager must not be Null");
		this.financeService = financeService;
	}

	@GetMapping("/finance")
	public String showFinancePage(Model model)
	{
		model.addAttribute("finance" , financeService.showAllFinance());
		return "finance";
	}
}
