package missmint.rooms;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Entity
public class Room {

	@Id
	@GeneratedValue
	private long id;
	private String name;

	@OneToMany(cascade = CascadeType.REMOVE)
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
