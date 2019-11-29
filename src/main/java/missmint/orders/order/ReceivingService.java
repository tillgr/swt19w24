package missmint.orders.order;

import missmint.inventory.manager.MaterialManager;
import missmint.inventory.products.Material;
import missmint.inventory.products.OrderItem;
import missmint.orders.service.MissMintService;
import missmint.orders.service.ServiceCategory;
import missmint.orders.service.ServiceManager;
import missmint.orders.service.ServiceConsumptionManager;

import missmint.time.EntryRepository;
import missmint.time.TimeTableEntry;
import missmint.time.TimeTableService;
import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.accountancy.AccountancyEntry;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.order.OrderManager;
import org.salespointframework.quantity.Quantity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class ReceivingService {
	private final OrderService orderService;
	private final OrderManager<MissMintOrder> orderManager;
	private final TimeTableService timeTableService;
	private final Catalog<OrderItem> itemCatalog;
	private EntryRepository entryRepository;
	private final UniqueInventory<UniqueInventoryItem> materialInventory;
	private final MaterialManager materialManager;
	private Accountancy accountancy;
	private final ServiceManager serviceManager;

	public ReceivingService(OrderService orderService, OrderManager<MissMintOrder> orderManager, TimeTableService timeTableService, Catalog<OrderItem> itemCatalog, EntryRepository entryRepository, UniqueInventory<UniqueInventoryItem> materialInventory, MaterialManager materialManager, Accountancy accountancy, ServiceManager serviceManager) {
		this.orderService = orderService;
		this.orderManager = orderManager;
		this.timeTableService = timeTableService;
		this.itemCatalog = itemCatalog;
		this.entryRepository = entryRepository;
		this.materialInventory = materialInventory;
		this.materialManager = materialManager;
		this.accountancy = accountancy;
		this.serviceManager = serviceManager;
	}

	public void receiveOrder(MissMintOrder order) {
		MissMintService service = serviceManager.getService(order);
		Assert.isTrue(orderService.isOrderAcceptable(service), "service must be acceptable");
		itemCatalog.save(order.getItem());

		order.getOrderLines().forEach(orderLine ->
			accountancy.add(new AccountancyEntry(orderLine.getPrice(), orderLine.getProductName()))
		);

		TimeTableEntry entry = timeTableService.createEntry(order);
		order.setExpectedFinished(entry.getDate());
		orderManager.save(order);
		entryRepository.save(entry);

		ServiceCategory serviceCategory = ServiceManager.getCategory(service);
		ServiceConsumptionManager.serviceMatRelation.get(serviceCategory).forEach(x ->
			{
				String materialName = x.getFirst();
				Quantity quantity = x.getSecond();
				Material material = materialManager.fromName(materialName);
				UniqueInventoryItem item = materialInventory.findByProduct(material).orElseThrow(() -> new RuntimeException("could not find inventory item"));
				materialManager.consume(item.getId(), quantity.getAmount().intValue());
				materialManager.autoRestock(item);
			}
		);

	}
}
