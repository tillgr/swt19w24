package missmint.inventory.manager;

import missmint.inventory.products.Material;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import org.springframework.data.util.Streamable;
import org.springframework.transaction.annotation.Transactional;


import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.salespointframework.core.Currencies.EURO;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootTest
@Transactional
public class MaterialManagerTest {

	@Autowired
	private Catalog<Material> materialCatalog;
	@Autowired
	private UniqueInventory<UniqueInventoryItem> materialInventory;
	@Autowired
	private MaterialManager materialManager;


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
	void consumeMaterialWithoutCheckTest() {
		Streamable<Material> sqMaterialStreamable = materialCatalog.findByName("sqTest");
		Optional<UniqueInventoryItem> sqMaterial = materialInventory.findByProductIdentifier(Objects.requireNonNull(sqMaterialStreamable.toList().get(0).getId()));

		sqMaterial.ifPresent(it -> {
			UniqueInventoryItem item = materialManager.checkAndConsume(it.getId(),20);
			assertThat(item.getQuantity().getAmount().intValueExact()).isEqualTo(80);
		});
	}

	@Test
	void consumeMaterialCheck(){
		Streamable<Material> lMaterialStreamable = materialCatalog.findByName("lTest");
		Optional<UniqueInventoryItem> lMaterial = materialInventory.findByProductIdentifier(Objects.requireNonNull(lMaterialStreamable.toList().get(0).getId()));

		lMaterial.ifPresent(material -> {
			UniqueInventoryItem item = materialManager.checkAndConsume(material.getId(),1000);
			assertThat(item.getQuantity().getAmount().intValueExact()).isEqualTo(100);
		});
	}

	@Test
	void restockLessThanMaxMaterial(){
		Streamable<Material> uMaterialStreamable = materialCatalog.findByName("uTest");
		Optional<UniqueInventoryItem> uMaterial = materialInventory.findByProductIdentifier(Objects.requireNonNull(uMaterialStreamable.toList().get(0).getId()));

		uMaterial.ifPresent(material -> {
			UniqueInventoryItem item = materialManager.checkAndRestock(material.getId(),900);
			assertThat(item.getQuantity().getAmount().intValueExact()).isEqualTo(1000);
		});
	}

	@Test
	void restockMoreThanMaxMaterial(){
		Streamable<Material> mMaterialStreamable = materialCatalog.findByName("mTest");
		Optional<UniqueInventoryItem> mMaterial = materialInventory.findByProductIdentifier(Objects.requireNonNull(mMaterialStreamable.toList().get(0).getId()));

		mMaterial.ifPresent(material -> {
			UniqueInventoryItem item = materialManager.checkAndRestock(material.getId(),100000);
			assertThat(item.getQuantity().getAmount().intValueExact()).isEqualTo(100);
		});
	}

	@Test
	void fromNameExceptionTest(){
		assertThrows(RuntimeException.class, () -> materialManager.fromName("NotInInventory"));
	}


}

