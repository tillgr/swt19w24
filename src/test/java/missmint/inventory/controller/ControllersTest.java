package missmint.inventory.controller;

import missmint.inventory.forms.MaterialForm;
import missmint.inventory.products.Material;
import missmint.inventory.products.OrderItem;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.inventory.InventoryItemIdentifier;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.util.Pair;
import org.springframework.data.util.Streamable;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.math.BigInteger;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.*;
import static org.salespointframework.core.Currencies.EURO;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class ControllersTest {

	@Autowired
	MockMvc mvc;
	@Autowired
	InventoryController inventoryController;
	@Autowired
	OrderItemController orderItemController;
	@Autowired
	private Catalog<Material> materialCatalog;
	@Autowired
	private Catalog<OrderItem> orderItemCatalog;
	@Autowired
	private UniqueInventory<UniqueInventoryItem> materialInventory;

	@BeforeEach
	public void setUp() {
		Stream.of(
				Pair.of(new Material("uTest", Money.of(0.5, EURO), Metric.UNIT), "UNIT_MATERIAL"),
				Pair.of(new Material("mTest", Money.of(0.02, EURO), Metric.METER), "METER_MATERIAL"),
				Pair.of(new Material("sqTest", Money.of(0.02, EURO), Metric.SQUARE_METER), "SQUARE_METER_MATERIAL"),
				Pair.of(new Material("lTest", Money.of(1, EURO), Metric.LITER), "LITER_MATERIAL")
		).forEach(materialStringPair -> {
			materialStringPair.getFirst().addCategory(materialStringPair.getSecond());
			materialCatalog.save(materialStringPair.getFirst());
		});

		OrderItem orderItem = (new OrderItem("orderItemTest"));
		orderItemCatalog.save(orderItem);

		materialCatalog.findByCategory("UNIT_MATERIAL").forEach(material -> {
			if (materialInventory.findByProduct(material).isEmpty()) {
				materialInventory.save(new UniqueInventoryItem(material, Quantity.of(100, Metric.UNIT)));
			}
		});
		materialCatalog.findByCategory("SQUARE_METER_MATERIAL").forEach(material -> {
			if (materialInventory.findByProduct(material).isEmpty()) {
				materialInventory.save(new UniqueInventoryItem(material, Quantity.of(100, Metric.SQUARE_METER)));
			}
		});
		materialCatalog.findByCategory("METER_MATERIAL").forEach(material -> {
			if (materialInventory.findByProduct(material).isEmpty()) {
				materialInventory.save(new UniqueInventoryItem(material, Quantity.of(100, Metric.METER)));
			}
		});
		materialCatalog.findByCategory("LITER_MATERIAL").forEach(material -> {
			if (materialInventory.findByProduct(material).isEmpty()) {
				materialInventory.save(new UniqueInventoryItem(material, Quantity.of(100, Metric.LITER)));
			}
		});
	}

	@Test
	void preventsPublicAccessInventoryController() throws Exception {
		mvc.perform(get("/material"))
				.andExpect(status().isFound())
				.andExpect(header().string(HttpHeaders.LOCATION, endsWith("/login")));
		mvc.perform(get("/material/consume"))
				.andExpect(status().is(405));
		mvc.perform(get("/material/restock"))
				.andExpect(status().is(405));
	}

	@Test
	@WithMockUser(username = "user", roles = "ADMIN")
	void accessibleForAdminInventoryController() throws Exception {

		mvc.perform(get("/material")) //
				.andExpect(status().isOk()) //
				.andExpect(model().attributeExists("material"));
	}

	@Test
	void preventsPublicAccessOrderItemController() throws Exception {
		mvc.perform(get("/orderItem"))
				.andExpect(status().isFound())
				.andExpect(header().string(HttpHeaders.LOCATION, endsWith("/login")));
	}

	@Test
	@WithMockUser(username = "user", roles = "ADMIN")
	void accessibleForAdminOrderItemController() throws Exception {
		mvc.perform(get("/orderItem"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("orders"));
	}

	@Test
	@WithMockUser(username = "user", roles = "ADMIN" )
	void consumeAndRestockTest() throws Exception{

		Streamable<Material> product = materialCatalog.findByName("uTest");
		Material productMaterial = product.toList().get(0);
		Optional<UniqueInventoryItem> itemMaterial = materialInventory.findByProduct(productMaterial);
		InventoryItemIdentifier itemMaterialId = materialInventory.findByProduct(productMaterial).get().getId();

		BigInteger big20 = new BigInteger("20");
		var form = new MaterialForm(big20,itemMaterialId);;
		Model model = new ExtendedModelMap();
		Errors errors = new BeanPropertyBindingResult(form, "form");
		inventoryController.consume(form, errors, model);
		inventoryController.restock(form, errors, model);

		mvc.perform(post("/material/consume").locale(Locale.ROOT).with(csrf())
				.param("number","50")
		)
				.andExpect(status().isOk())
				.andExpect(view().name("material"))
				.andExpect(content().string(containsString("material")));

		mvc.perform(post("/material/restock").locale(Locale.ROOT).with(csrf())
				.param("number","50")
		)
				.andExpect(status().isOk())
				.andExpect(view().name("material"))
				.andExpect(content().string(containsString("material")));
	}

	@Test
	@WithMockUser
	void materialFormRestockErrorsTest() throws Exception {
		mvc.perform(post("/material/restock").locale(Locale.ROOT).with(csrf())
				.param("number","-1")
		)
				.andExpect(status().isOk())
				.andExpect(view().name("material"))
				.andExpect(content().string(containsString("Minimum amount to restock/consume is 1.")));

		mvc.perform(post("/material/restock").locale(Locale.ROOT).with(csrf())
				.param("number","10001")
		)
				.andExpect(status().isOk())
				.andExpect(view().name("material"))
				.andExpect(content().string(containsString("Maximum amount to restock/consume is 10000.")));

		}

	@Test
	@WithMockUser
	void materialFormConsumeErrorsTest() throws Exception {
		mvc.perform(post("/material/consume").locale(Locale.ROOT).with(csrf())
				.param("number","-1")
		)
				.andExpect(status().isOk())
				.andExpect(view().name("material"))
				.andExpect(content().string(containsString("Minimum amount to restock/consume is 1.")));

		mvc.perform(post("/material/consume").locale(Locale.ROOT).with(csrf())
				.param("number","10001")
		)
				.andExpect(status().isOk())
				.andExpect(view().name("material"))
				.andExpect(content().string(containsString("Maximum amount to restock/consume is 10000.")));
	}

	@Test
	@WithMockUser(username = "user", roles = "ADMIN" )
	void removeOrderItemTest(){
		ProductIdentifier productIdentifier = orderItemCatalog.findByName("orderItemTest").iterator().next().getId();
		orderItemController.remove(productIdentifier);
	}
}
