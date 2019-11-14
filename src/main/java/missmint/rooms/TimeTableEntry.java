package missmint.rooms;

import javax.persistence.*;
import java.util.Optional;

@Entity
public class TimeTableEntry {
	private @Id @GeneratedValue long id;
	private int start;
	private int end;

	private Booking booking;

	@ManyToOne
	private Room room;
	//private Order order;

	public TimeTableEntry (int start, int end, Room room){
		this.start = start;
		this.end = end;
		this.room = room;
		this.booking = Booking.FREE;
	}

	private TimeTableEntry(){
		this.start = 0;
		this.end = 0;
		this.room = null;
	}

	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public void setEnd(int end) {
		this.end = end;
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

	public Booking getBooking() {
		return booking;
	}

	public void setBooking(Booking booking) {
		this.booking = booking;
	}

}
