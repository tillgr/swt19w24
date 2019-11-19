package missmint.rooms;

import com.mysema.commons.lang.Pair;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class Slot {
	private @GeneratedValue @Id long id;
	private Pair<Integer, Integer> slots[]  = new Pair[5];

	public Slot (long id, Pair<Integer, Integer> slots[]){
		slots[0] = new Pair<>(1,2);
		slots[1] = new Pair<>(2,3);
		slots[2] = new Pair<>(3,4);
		slots[3] = new Pair<>(4,5);
		slots[4] = new Pair<>(5,6);

		this.slots=slots;
		this.id = id;
	}


	public long getId() {
		return id;
	}

	public Pair<Integer, Integer>[] getSlots() {
		return slots;
	}
}
