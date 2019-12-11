package missmint.rooms;

import missmint.time.TimeTableEntry;
import org.hibernate.dialect.Database;

import javax.persistence.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 * The room decribing a place, where a order is processed by the staff
 */

@Entity
public class Room {
	@Id
	@GeneratedValue
	private long id;
	private String name;

	/**
	 * Stores all entries associated with the room.
	 */
	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "room")
	private Set<TimeTableEntry> entrySet;

	public Room() {
	}
	/**
	 * Create a new room.
	 *
	 * @param name The name of the created room.
	 */

	public Room(String name) {
		this.name = name;
		entrySet = new HashSet<>();
	}

	/**
	 * @return The name of the room.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return The id of the room.
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return All entries associated to this room.
	 */
	public Set<TimeTableEntry> getEntrySet() {
		return entrySet;
		//return Database.entrySet.sort(Comparator.comparing(TimeTableEntry :: getSlot)); //TODO welche Database importieren?
	}
}
