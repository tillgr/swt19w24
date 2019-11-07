package missmint.rooms;

public class TimeTableEntry {
	private int from;
	private int to;
	private Room room;
	private Order order;

	public TimeTableEntry (int from, int to, Room room, Order order){
		this.from = from;
		this.to = to;
		this.room = room;
		this.order = order;
	}

	//Getter
	public int getFrom() {
		return from;
	}

	public int getTo() {
		return to;
	}

	public Room getRoom() {
		return room;
	}

	public Order getOrder() {
		return order;
	}

	//Setter
	public void setFrom(int from) {
		this.from = from;
	}

	public void setTo(int to) {
		this.to = to;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
}
