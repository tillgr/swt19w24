package missmint.inventory.controller;

import missmint.inventory.forms.MaterialForm;
import missmint.inventory.manager.MaterialManager;
import org.salespointframework.inventory.InventoryItemIdentifier;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class InventoryController {


	private final MaterialManager materialManager;
	private final UniqueInventory<UniqueInventoryItem> materialInventory;

	public InventoryController(MaterialManager materialManager, UniqueInventory<UniqueInventoryItem> materialInventory) {
		this.materialManager = materialManager;
		this.materialInventory = materialInventory;
	}

	/**
	 * Lists all materials in the system.
	 *
	 * @return The material template.
	 */
	@GetMapping("/material")
	@PreAuthorize("isAuthenticated()")
	public String material(Model model, @ModelAttribute("form") MaterialForm materialForm) {
		model.addAttribute("material", materialManager.findMaterials());
		return "material";
	}

	/**
	 * @param materialForm Form for Restocking/Consumption of material
	 * @return The material template after changing the stock by the specified amount
	 */
	@PostMapping("/material/consume")
	@PreAuthorize("isAuthenticated()")
	public String consume(@Valid @ModelAttribute("form") MaterialForm materialForm, Errors errors, Model model) {
		if (errors.hasErrors()) {
			return material(model, materialForm);
		}
		InventoryItemIdentifier materialId = materialForm.getMaterialId();
		int number = materialForm.getNumber().intValueExact();

		if (materialInventory.findById(materialId).get().getQuantity().getAmount().intValueExact() < number) {
			model.addAttribute("consumeError", true);
			return material(model, materialForm);
		}
		materialManager.checkAndConsume(materialId, number);
		return "redirect:/material";
	}

	/**
	 * @param materialForm Form for Restocking/Consumption of material
	 * @return The material template after changing the stock by the specified amount
	 */
	@PostMapping("/material/restock")
	@PreAuthorize("isAuthenticated()")
	public String restock(@Valid @ModelAttribute("form") MaterialForm materialForm, Errors errors, Model model) {
		if (errors.hasErrors()) {
			return material(model, materialForm);
		}
		InventoryItemIdentifier materialId = materialForm.getMaterialId();
		int number = materialForm.getNumber().intValueExact();
		if (materialInventory.findById(materialId).get().getQuantity().getAmount().intValueExact() + number > 10000) {
			model.addAttribute("restockError", true);
			return material(model, materialForm);
		}
		materialManager.checkAndRestock(materialId, number);
		return "redirect:/material";
	}
}

