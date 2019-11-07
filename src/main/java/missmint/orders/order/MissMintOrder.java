package missmint.orders.order;

import missmint.orders.services.Service;
import org.salespointframework.order.Order;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;

@Entity
public class MissMintOrder {
	private @Id @GeneratedValue long id;
	private String customer;
	private LocalDate inbound;
	private LocalDate expectedFinished;
	private LocalDate finished;

	@OneToOne
	private Order order;

	public MissMintOrder() {
	}

	public MissMintOrder(String customer, Order order, LocalDate inbound) {
		this.customer = customer;
		this.order = order;
		this.inbound = inbound;
	}
}
