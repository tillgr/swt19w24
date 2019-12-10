package missmint.finance;

import org.salespointframework.accountancy.Accountancy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class FinanceController {
	private FinanceService financeService;
	private Accountancy accountancy;

	FinanceController(FinanceService financeService, Accountancy accountancy) {
		Assert.notNull(financeService, "Finance Manager must not be Null");
		this.accountancy = accountancy;
		this.financeService = financeService;
	}

	@GetMapping("/finance")
	@PreAuthorize("hasRole('ADMIN')")
	public String showFinancePage(Model model) {
		model.addAttribute("finance", accountancy.findAll());
		model.addAttribute("sum", financeService.getSum(accountancy.findAll()));

		return "finance";
	}

	@GetMapping("/finance/month")
	@PreAuthorize("hasRole('ADMIN')")
	public String showLastMonth(Model model) {
		model.addAttribute("finance", financeService.lastMonth());
		model.addAttribute("sum", financeService.getSum(financeService.lastMonth()));
		model.addAttribute("lastMonth", true);

		return "finance";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/finance/addItem")
	public String financeForm(Model model, AddFinanceForm form) {
		model.addAttribute("form", form);
		return "addItem";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/finance/addItem")
	public String addItemInForm(@Valid AddFinanceForm form, Errors result) {
		if (result.hasErrors()) {
			return "addItem";
		}
		financeService.createFinanceItemForm(form);
		return "redirect:/finance";
	}
}
