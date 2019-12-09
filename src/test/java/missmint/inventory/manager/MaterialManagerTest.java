package missmint.inventory.manager;

import missmint.inventory.products.Material;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
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
import org.springframework.test.context.event.annotation.BeforeTestExecution;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.salespointframework.core.Currencies.EURO;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MaterialManagerTest {

	@Autowired
	private Catalog<Material> materialTestCatalog;
	@Autowired
	private UniqueInventory<UniqueInventoryItem> materialTestInventory;
	@Autowired
	private MaterialManager materialTestManager;

	@BeforeTestExecution
	public void setUp(){
		Stream.of(
				Pair.of(new Material("uTest", Money.of(0.5, EURO), Metric.UNIT), "UNIT_MATERIAL"),
				Pair.of(new Material("mTest", Money.of(0.02,EURO), Metric.METER), "METER_MATERIAL"),
				Pair.of(new Material("sqTest", Money.of(0.02,EURO), Metric.SQUARE_METER), "SQUARE_METER_MATERIAL"),
				Pair.of(new Material("lTest", Money.of(1,EURO), Metric.LITER), "LITER_MATERIAL")
		).forEach(materialStringPair-> {
			materialStringPair.getFirst().addCategory(materialStringPair.getSecond());
			materialTestCatalog.save(materialStringPair.getFirst());
		});
			materialTestCatalog.findByCategory("UNIT_MATERIAL").forEach(material -> {
				if (materialTestInventory.findByProduct(material).isEmpty()) {
					materialTestInventory.save(new UniqueInventoryItem(material, Quantity.of(100, Metric.UNIT)));
				}
			});
			materialTestCatalog.findByCategory("SQUARE_METER_MATERIAL").forEach(material -> {
				if (materialTestInventory.findByProduct(material).isEmpty()) {
					materialTestInventory.save(new UniqueInventoryItem(material, Quantity.of(100, Metric.SQUARE_METER)));
				}
			});
			materialTestCatalog.findByCategory("METER_MATERIAL").forEach(material -> {
				if (materialTestInventory.findByProduct(material).isEmpty()) {
					materialTestInventory.save(new UniqueInventoryItem(material, Quantity.of(100, Metric.METER)));
				}
			});
			materialTestCatalog.findByCategory("LITER_MATERIAL").forEach(material -> {
				if (materialTestInventory.findByProduct(material).isEmpty()) {
					materialTestInventory.save(new UniqueInventoryItem(material, Quantity.of(100, Metric.LITER)));
				}
			});
		}

		@Test
		public void consumeTest() {
			setUp();
			Streamable<Material> uMaterial = materialTestCatalog.findByName("uTest");
			Optional<UniqueInventoryItem> i1 = materialTestInventory.findByProductIdentifier(Objects.requireNonNull(uMaterial.toList().get(0).getId()));
			i1.ifPresent(test -> {
				int i = materialTestManager.manualConsume(test,20);
				assertThat(test.getQuantity().getAmount().intValueExact() - i).isEqualTo(80);
			});
		}

		@Test
		public void consumeAutoRestockTest(){
		setUp();
			Streamable<Material> material = materialTestCatalog.findByName("uTest");
			System.out.println(material.toList());
			Optional<UniqueInventoryItem> i1 = materialTestInventory.findByProductIdentifier(Objects.requireNonNull(material.toList().get(0).getId()));
			i1.ifPresent(test2 -> {
				int i2 = materialTestManager.manualConsume(test2,110);
				assertThat(test2.getQuantity().getAmount().intValueExact() - i2).isEqualTo(0);
			});
		}

	}

