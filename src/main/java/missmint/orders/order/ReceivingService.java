package missmint.orders.order;

import missmint.inventory.products.OrderItem;
import missmint.orders.service.MissMintService;
import missmint.orders.service.ServiceService;
import missmint.time.EntryRepository;
import missmint.time.TimeTableEntry;
import missmint.time.TimeTableService;
import org.salespointframework.catalog.Catalog;
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

	public ReceivingService(ServiceService serviceService, OrderService orderService, OrderManager<MissMintOrder> orderManager, TimeTableService timeTableService, Catalog<OrderItem> itemCatalog, EntryRepository entryRepository) {
		this.serviceService = serviceService;
		this.orderService = orderService;
		this.orderManager = orderManager;
		this.timeTableService = timeTableService;
		this.itemCatalog = itemCatalog;
		this.entryRepository = entryRepository;
	}

	public void receiveOrder(MissMintOrder order) {
		MissMintService service = serviceService.getService(order);
		Assert.isTrue(orderService.isOrderAcceptable(service), "service must be acceptable");
		itemCatalog.save(order.getItem());

		TimeTableEntry entry = timeTableService.createEntry(order);
		order.setExpectedFinished(entry.getDate());
		orderManager.save(order);
		entryRepository.save(entry);

		// TODO use material, finance
	}
}
