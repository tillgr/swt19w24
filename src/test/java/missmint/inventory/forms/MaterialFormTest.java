package missmint.inventory.forms;

import missmint.inventory.products.Material;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.inventory.InventoryItemIdentifier;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import org.springframework.data.util.Streamable;
import org.springframework.validation.Errors;

import javax.validation.Validation;
import javax.validation.Validator;
import java.math.BigInteger;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.salespointframework.core.Currencies.EURO;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootTest
public class MaterialFormTest {

	private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	@Autowired
	private Catalog<Material> materialCatalog;
	@Autowired
	private UniqueInventory<UniqueInventoryItem> materialInventory;

	BigInteger big20 = new BigInteger("20");
	BigInteger big10001 = new BigInteger("10001");
	BigInteger big0 = new BigInteger("0");

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
	void getterCoverage(){
		Streamable<Material> product = materialCatalog.findByName("uTest");
		Material productMaterial = product.toList().get(0);
		InventoryItemIdentifier itemMaterialId = materialInventory.findByProduct(productMaterial).get().getId();
		var form = new MaterialForm(big20,itemMaterialId);

		assertThat(form.getMaterialId()).isEqualTo(itemMaterialId);
		assertThat(form.getNumber()).isEqualTo(big20);

	}
	@Test
	void validForm() {
		Streamable<Material> product = materialCatalog.findByName("uTest");
		Material material = product.toList().get(0);
		Optional<UniqueInventoryItem> item = materialInventory.findByProduct(material);
		var form = new MaterialForm(big20,item.get().getId());
		var violations = validator.validate(form);
		Assertions.assertTrue(violations.isEmpty());
	}

	@Test
	void nonValidNumberLow(){
		Streamable<Material> product = materialCatalog.findByName("uTest");
		Material material = product.toList().get(0);
		Optional<UniqueInventoryItem> item = materialInventory.findByProduct(material);
		var form = new MaterialForm(big0,item.get().getId());
		var violations = validator.validate(form);
		Assertions.assertEquals(0, violations.size());
	}

	@Test
	void nonValidNumberHigh(){
		Streamable<Material> product = materialCatalog.findByName("uTest");
		Material material = product.toList().get(0);
		Optional<UniqueInventoryItem> item = materialInventory.findByProduct(material);
		var form = new MaterialForm(big10001,item.get().getId());
		var violations = validator.validate(form);
		Assertions.assertEquals(1, violations.size());
	}


}