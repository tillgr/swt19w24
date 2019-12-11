package missmint.time;

import missmint.orders.order.MissMintOrder;
import missmint.rooms.Room;
import missmint.users.model.Staff;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entry represents a booked Timeslot
 */
@Entity
public class TimeTableEntry {
	@Id
	@GeneratedValue
	private long id;
	@OneToOne
	@JoinColumn(name = "missmintorder")
	private MissMintOrder order;
	@ManyToOne
	private Staff staff;
	private int slot;
	@ManyToOne
	private Room room;
	private LocalDate date;

	public TimeTableEntry(MissMintOrder order, Staff staff, int slot, Room room, LocalDate date) {
		this.order = order;
		this.staff = staff;
		this.slot = slot;
		this.room = room;
		this.date = date;
	}

	public TimeTableEntry() {
	}

	public Room getRoom() {
		return room;
	}

	public long getId() {
		return id;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public int getSlot() {
		return slot;
	}

	public void setSlot(int slot) {
		this.slot = slot;
	}

	public MissMintOrder getOrder() {
		return order;
	}

	public LocalDate getDate() {
		return date;
	}

	public LocalDateTime getBeginning() {
		return LocalDateTime.of(date, TimeTableService.SLOTS.get(slot).getFirst());
	}

	public LocalDateTime getEnd() {
		return LocalDateTime.of(date, TimeTableService.SLOTS.get(slot).getSecond());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof TimeTableEntry)) return false;
		TimeTableEntry that = (TimeTableEntry) o;
		return id == that.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
