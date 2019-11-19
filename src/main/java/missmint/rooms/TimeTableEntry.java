package missmint.rooms;

import missmint.orders.order.MissMintOrder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Optional;
import java.util.Date;

@Entity
public class TimeTableEntry {
	private @Id @GeneratedValue long id;
	private MissMintOrder order;
	//private Mitarbeiter mitarbeiter;	//TODO von kien einfügen
	private long slotId;	//TODO noch hinzufügen

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


}
