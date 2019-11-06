package missmint.orders.order;

import missmint.orders.services.Service;
import org.salespointframework.order.Order;

import javax.persistence.*;
import java.sql.Date;

@Entity
public class MissMintOrder {
	private @Id @GeneratedValue long id;
	private String customer;
	private Date inbound;
	private Date expectedFinished;
	private Date finished;

	@ManyToOne
	private Service service;

	@OneToOne
	private Order order;

	public MissMintOrder() {
	}
}
