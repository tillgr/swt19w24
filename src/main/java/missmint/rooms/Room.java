package missmint.rooms;

import missmint.time.TimeTableEntry;
import org.hibernate.dialect.Database;

import javax.persistence.*;
import java.sql.Time;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Room {
	@Id
	@GeneratedValue
	private long id;
	private String name;

	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "room")
	private Set<TimeTableEntry> entrySet;

	public Room() {
	}

	public Room(String name) {
		this.name = name;
		entrySet = new HashSet<>();
	}

	public String getName() {
		return name;
	}

	public long getId() {
		return id;
	}

	public Set<TimeTableEntry> getEntrySet() {
		return entrySet;
	}
}
