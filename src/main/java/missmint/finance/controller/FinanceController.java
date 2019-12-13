package missmint.finance.controller;

import missmint.finance.form.AddFinanceForm;
import missmint.finance.service.FinanceService;
import org.salespointframework.accountancy.Accountancy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import javax.validation.Valid;

/**
 * Controller for the overview and detail pages of the finance.
 */
@Controller
public class FinanceController {
	private FinanceService financeService;
	private Accountancy accountancy;

	FinanceController(FinanceService financeService, Accountancy accountancy) {
		Assert.notNull(financeService, "Finance Manager must not be Null");
		this.accountancy = accountancy;
		this.financeService = financeService;
	}

	/**
	 * Lists all finance items in the system.
	 *
	 * @return finance template.
	 */
	@GetMapping("/finance")
	@PreAuthorize("hasRole('ADMIN')")
	public String showFinancePage(Model model) {
		model.addAttribute("finance", accountancy.findAll());
		model.addAttribute("sum", financeService.getSum());
		return "finance";
	}

	/**
	 * shows a form for adding new finance items.
	 *
	 * @return addItem template.
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/finance/addItem")
	public String financeForm(Model model, AddFinanceForm form) {
		model.addAttribute("form", form);
		return "addItem";
	}

	/**
	 * uses for creating new finance items.
	 *
	 * @param form used for creating new finance items
	 * @param result errors in form
	 * @return finance overview
	 */
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
