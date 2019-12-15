package missmint.inventory.manager;

import missmint.inventory.products.OrderItem;
import missmint.orders.order.MissMintOrder;
import missmint.orders.order.OrderState;
import missmint.time.EntryRepository;
import missmint.time.TimeTableEntry;
import missmint.time.TimeTableService;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.order.OrderManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
public class OrderItemManager {

	private final OrderManager<MissMintOrder> orderManager;
	private final Catalog<OrderItem> orderItemCatalog;
	private final EntryRepository entryRepository;
	private final TimeTableService timeTableService;

	public OrderItemManager(OrderManager<MissMintOrder> orderManager, Catalog<OrderItem> orderItemCatalog, EntryRepository entryRepository, TimeTableService timeTableService) {
		this.orderManager = orderManager;
		this.orderItemCatalog = orderItemCatalog;
		this.entryRepository = entryRepository;
		this.timeTableService = timeTableService;
	}

	public void deleteOrderItem(ProductIdentifier orderItemId) {
		Page<MissMintOrder> orders = orderManager.findAll(Pageable.unpaged());
		OrderItem test = new OrderItem("Placeholder");
		OrderItem item = orderItemCatalog.findById(orderItemId).orElse(test);

		orders.forEach(order -> {
			if (Objects.equals(order.getItem(), item)) {
				order.setItem(null);
				orderItemCatalog.deleteById(orderItemId);
				if (!order.getOrderState().equals(OrderState.PICKED_UP)){
					order.setEntry(null);
					entryRepository.deleteTimeTableEntriesByOrder(order);
					timeTableService.rebuildTimeTable();
				}
				order.setOrderState(OrderState.PICKED_UP);
				orderManager.save(order);
			}
		});
	}
}