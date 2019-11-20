package missmint.rooms;

import missmint.orders.order.MissMintOrder;
import missmint.users.model.Staff;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class TimeTableEntry {
	private @Id @GeneratedValue long id;
	@ManyToOne
	private MissMintOrder order;
	@ManyToMany		//TODO stimmt das?
	private Staff staff;
	private int slotsPos;

	@ManyToOne
	private Room room;

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

	public int getSlotsPos() {
		return slotsPos;
	}

	public void setSlotsPos(int slotsPos) {
		this.slotsPos = slotsPos;
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	public MissMintOrder getOrder() {
		return order;
	}
}
