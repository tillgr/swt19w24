package missmint.rooms;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class TimeTableEntry {
	private @Id @GeneratedValue long id;
	private int from;
	private int to;
	//private Room room;
	//private Order order;

	public TimeTableEntry (int from, int to){
		this.from = from;
		this.to = to;
	}

}
