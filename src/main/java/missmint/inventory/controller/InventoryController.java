package missmint.inventory.controller;

import missmint.inventory.forms.MaterialForm;
import missmint.inventory.manager.MaterialManager;
import org.salespointframework.inventory.InventoryItemIdentifier;
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

	public InventoryController(MaterialManager materialManager) {
		this.materialManager = materialManager;
	}

	private final MaterialManager materialManager;

	/**
	 *	Lists all materials in the system.
	 *
	 * @return The material template.
	 */

	@GetMapping("/material")
	@PreAuthorize("isAuthenticated()")
	public String material(Model model) {
		model.addAttribute("material", materialManager.findMaterials());
		return "material";
	}

	/**
	 * @param materialForm
	 * @return The material template after changing the stock by the specified amount
	 */
	@PostMapping("/material/consume")
	@PreAuthorize("isAuthenticated()")
	public String consume(@Valid @ModelAttribute("form") MaterialForm materialForm){
		InventoryItemIdentifier materialId = materialForm.getMaterialId();
		int number = materialForm.getNumber();
		materialManager.checkAndConsume(materialId, number);
		return "redirect:/material";
	}

	/**
	 * @param materialForm
	 * @return The material template after changing the stock by the specified amount
	 */
	@PostMapping("/material/restock")
	@PreAuthorize("isAuthenticated()")
	public String restock(@Valid @ModelAttribute("form") MaterialForm materialForm){
		InventoryItemIdentifier materialId = materialForm.getMaterialId();
		int number = materialForm.getNumber();
		materialManager.checkAndRestock(materialId, number);
		return "redirect:/material";
	}
}

