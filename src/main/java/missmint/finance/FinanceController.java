package missmint.finance;

import org.javamoney.moneta.Money;
import org.salespointframework.accountancy.AccountancyEntry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.StreamUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.money.MonetaryAmount;
import javax.validation.Valid;
import java.util.ArrayList;

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
		model.addAttribute("sum", financeService.getSum());
		return "finance";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/finance/addItem")
	public String financeForm (Model model, AddFinanceForm form){
		model.addAttribute("form", form);
		return "addItem";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/finance/addItem")
	public String addItemInForm (@Valid AddFinanceForm form, Errors result){
		if (result.hasErrors()){
			return "addItem";
		}
		financeService.createFinanceItemForm(form);
		return "redirect:/finance";
	}

}
