package missmint.orders.order;

/**
 * The states an order can be in.
 *
 * @see MissMintOrder
 */
public enum OrderState {
	/**
	 * The order is waiting for repair.
	 */
	WAITING,
	/**
	 * The order is currently in repair.
	 */
	IN_PROGRESS,
	/**
	 * The repair is finished.
	 */
	FINISHED,
	/**
	 * The customer's item is stored because it was not picked up.
	 */
	STORED,
	/**
	 * The item was picked up by the customer.
	 */
	PICKED_UP,
	/**
	 * The customer's item is given to charity.
	 */
	CHARITABLE_USED
}