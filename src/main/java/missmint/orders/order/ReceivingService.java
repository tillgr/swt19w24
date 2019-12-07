package missmint.orders.order;

import missmint.finance.FinanceService;
import missmint.inventory.manager.MaterialManager;
import missmint.inventory.products.Material;
import missmint.inventory.products.OrderItem;
import missmint.orders.service.MissMintService;
import missmint.orders.service.ServiceCategory;
import missmint.orders.service.ServiceConsumptionManager;
import missmint.orders.service.ServiceManager;
import missmint.time.EntryRepository;
import missmint.time.TimeTableEntry;
import missmint.time.TimeTableService;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.order.OrderManager;
import org.salespointframework.quantity.Quantity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * A service that handles the receiving process.
 * @see missmint.orders.controllers.ReceivingController
 */
@Service
public class ReceivingService {
	private final OrderService orderService;
	private final OrderManager<MissMintOrder> orderManager;
	private final TimeTableService timeTableService;
	private final Catalog<OrderItem> itemCatalog;
	private EntryRepository entryRepository;
	private final UniqueInventory<UniqueInventoryItem> materialInventory;
	private final MaterialManager materialManager;
	private final FinanceService financeService;
	private final ServiceManager serviceManager;

	public ReceivingService(OrderService orderService, OrderManager<MissMintOrder> orderManager, TimeTableService timeTableService, Catalog<OrderItem> itemCatalog, EntryRepository entryRepository, UniqueInventory<UniqueInventoryItem> materialInventory, MaterialManager materialManager, FinanceService financeService, ServiceManager serviceManager) {
		this.orderService = orderService;
		this.orderManager = orderManager;
		this.timeTableService = timeTableService;
		this.itemCatalog = itemCatalog;
		this.entryRepository = entryRepository;
		this.materialInventory = materialInventory;
		this.materialManager = materialManager;
		this.financeService = financeService;
		this.serviceManager = serviceManager;
	}

	/**
	 * Saves the order, creates a time table entry, saves the customer's item and uses the needed material up.
	 *
	 * @param order The order that should be received. The order must be acceptable.
	 */
	public void receiveOrder(MissMintOrder order) {
		MissMintService service = serviceManager.getService(order);
		Assert.isTrue(orderService.isOrderAcceptable(service), "service must be acceptable");
		itemCatalog.save(order.getItem());

		order.getOrderLines().forEach(orderLine ->
			financeService.add(orderLine.getProductName(), orderLine.getPrice())
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
