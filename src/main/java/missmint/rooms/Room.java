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

	private @Id @GeneratedValue long id;
	//private Material UsedMaterial;
	private String name;
	private String service;

	@OneToMany(cascade = CascadeType.REMOVE)
	private Set<TimeTableEntry> entrySet;

	public Room (String name, String service, EntriesRepository entries, RoomsRepository rooms){
		//this.UsedMaterial = UsedMaterial;
		this.name = name;
		this.service = service;
		entrySet = new HashSet<>();
		rooms.save(this);
		for(int i=0; i<7; i++){
			TimeTableEntry entry = new TimeTableEntry(i, i+1, this);
			entrySet.add(entry);
			entries.save(entry);
		}
	}

	private Room (){
		//this.UsedMaterial = UsedMaterial;
		this.name = null;
		this.service = null;
	}

	//Getter
	/*public Material getUsedMaterial() {
		return UsedMaterial;
	}*/



	/*public int getRoomId() {
		return RoomId;
	}*/

	public String getBelegung() {
		return service;
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

	//Setter
	/*public void setUsedMaterial(Material material) {
		this.UsedMaterial = material;
	}*/


	/*public void setRoomId(int roomId) {
		RoomId = roomId;
	}*/
}
