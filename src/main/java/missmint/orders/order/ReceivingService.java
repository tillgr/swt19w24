package missmint.orders.order;

import missmint.Utils;
import missmint.inventory.manager.MaterialManager;
import missmint.inventory.products.Material;
import missmint.inventory.products.OrderItem;
import missmint.orders.service.MissMintService;
import missmint.orders.service.ServiceConsumptionManager;
import missmint.orders.service.ServiceService;
import missmint.time.EntryRepository;
import missmint.time.TimeTableEntry;
import missmint.time.TimeTableService;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.order.OrderManager;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class ReceivingService {
	private final ServiceService serviceService;
	private final OrderService orderService;
	private final OrderManager<MissMintOrder> orderManager;
	private final TimeTableService timeTableService;
	private final Catalog<OrderItem> itemCatalog;
	private EntryRepository entryRepository;
	private final UniqueInventory<UniqueInventoryItem> materialInventory;
	private final Catalog<Material> materialCatalog;
	private final MaterialManager materialManager;
	private final ServiceConsumptionManager serviceConsumptionManager;

	public ReceivingService(ServiceService serviceService, OrderService orderService, OrderManager<MissMintOrder> orderManager, TimeTableService timeTableService, Catalog<OrderItem> itemCatalog, EntryRepository entryRepository, UniqueInventory<UniqueInventoryItem> materialInventory, Catalog<Material> materialCatalog, MaterialManager materialManager, ServiceConsumptionManager serviceConsumptionManager) {
		this.serviceService = serviceService;
		this.orderService = orderService;
		this.orderManager = orderManager;
		this.timeTableService = timeTableService;
		this.itemCatalog = itemCatalog;
		this.entryRepository = entryRepository;
		this.materialInventory = materialInventory;
		this.materialCatalog = materialCatalog;
		this.materialManager = materialManager;
		this.serviceConsumptionManager = serviceConsumptionManager;
	}

	public void receiveOrder(MissMintOrder order, MaterialManager materialManager) {
		MissMintService service = serviceService.getService(order);
		Assert.isTrue(orderService.isOrderAcceptable(service), "service must be acceptable");
		itemCatalog.save(order.getItem());

		TimeTableEntry entry = timeTableService.createEntry(order);
		order.setExpectedFinished(entry.getDate());
		orderManager.save(order);
		entryRepository.save(entry);

		//automatisch nachbestellen

		serviceConsumptionManager.serviceMatRelation.get(service).stream().forEach(x -> materialManager.checkQuantity(x.getFirst()));

		// TODO use material, finance
	}
}
