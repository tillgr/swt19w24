package missmint.inventory.controller;

import missmint.inventory.forms.MaterialForm;
import missmint.inventory.products.Material;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.inventory.InventoryItemIdentifier;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import org.springframework.data.util.Streamable;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.salespointframework.core.Currencies.EURO;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class InventoryControllerTest {

	@Autowired
	MockMvc mvc;

	@Autowired
	InventoryController controller;

	@Autowired
	private Catalog<Material> materialCatalog;
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
		void preventsPublicAccess() throws Exception {

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
		void accessibleForAdmin() throws Exception {

			mvc.perform(get("/material")) //
					.andExpect(status().isOk()) //
					.andExpect(model().attributeExists("material"));
		}

		@Test
		@WithMockUser(username = "user", roles = "ADMIN" )
		void consumeTest(){
		Streamable<Material> product = materialCatalog.findByName("uTest");
		Material productMaterial = product.toList().get(0);
		Optional<UniqueInventoryItem> itemMaterial = materialInventory.findByProduct(productMaterial);
		InventoryItemIdentifier itemMaterialId = materialInventory.findByProduct(productMaterial).get().getId();

		//var form = new MaterialForm(20,itemMaterialId);
		//controller.consume(form);
		//controller.restock(form);
		}
}
