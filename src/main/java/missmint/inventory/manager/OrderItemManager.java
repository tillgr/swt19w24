package missmint.inventory.manager;

import missmint.inventory.products.OrderItem;
import missmint.orders.order.MissMintOrder;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.order.OrderManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class OrderItemManager {

	private final OrderManager<MissMintOrder> orderManager;
	private final Catalog<OrderItem> orderItemCatalog;

	public OrderItemManager(OrderManager<MissMintOrder> orderManager, Catalog<OrderItem> orderItemCatalog) {
		this.orderManager = orderManager;
		this.orderItemCatalog = orderItemCatalog;
	}


	public void deleteOrderItem(ProductIdentifier orderItemId) {
		Page<MissMintOrder> orders = orderManager.findAll(Pageable.unpaged());
			OrderItem test = new OrderItem("Placeholder");
			OrderItem item = orderItemCatalog.findById(orderItemId).orElse(test);

		orders.forEach(order -> {
			if (Objects.equals(order.getItem(), item)) {
				order.setItem(null);
				orderItemCatalog.deleteById(orderItemId);
			}
		});

	}
}