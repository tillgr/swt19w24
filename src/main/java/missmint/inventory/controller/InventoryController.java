package missmint.inventory.controller;

import missmint.inventory.forms.MaterialForm;
import missmint.inventory.manager.MaterialManager;
import org.salespointframework.inventory.InventoryItemIdentifier;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

	@GetMapping("/material")
	@PreAuthorize("isAuthenticated()")
	public String material(Model model) {
		model.addAttribute("material", materialManager.findMaterials());
		return "material";
	}

	@PostMapping("/material/consume")
	@PreAuthorize("isAuthenticated()")
	public String consume(@Valid @ModelAttribute("form") MaterialForm materialForm){
		InventoryItemIdentifier materialId = materialForm.getMaterialId();
		int number = materialForm.getNumber();
		materialManager.checkAndConsume(materialId, number);
		return "redirect:/material";
	}

	@PostMapping("/material/restock")
	@PreAuthorize("isAuthenticated()")
	public String restock(@Valid @ModelAttribute("form") MaterialForm materialForm){
		InventoryItemIdentifier materialId = materialForm.getMaterialId();
		int number = materialForm.getNumber();
		materialManager.checkAndRestock(materialId, number);
		return "redirect:/material";
	}
}

