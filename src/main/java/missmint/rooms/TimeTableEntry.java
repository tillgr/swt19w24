package missmint.rooms;

import missmint.orders.order.MissMintOrder;

import javax.persistence.*;

@Entity
public class TimeTableEntry {
	private @Id @GeneratedValue long id;
	@ManyToOne
	private MissMintOrder order;
	//private Mitarbeiter mitarbeiter;	//TODO von kien einfügen
	private long slotId;	//TODO noch hinzufügen
	private int pos;

	@ManyToOne
	private Room room;
	//private Order order;

	public TimeTableEntry (int start, int end, Room room){
		this.room = room;
	}

	private TimeTableEntry(){
		this.room = null;
	}

	public Room getRoom() {
		return room;
	}

	public long getId() {
		return id;
	}

	public void setRoom (Room room) {
		this.room = room;
	}

	public int getPos() {
		return pos;
	}

	public long getSlotId() {
		return slotId;
	}

	public MissMintOrder getOrder() {
		return order;
	}
}
