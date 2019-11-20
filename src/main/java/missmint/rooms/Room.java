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

	@OneToMany(cascade = CascadeType.REMOVE)
	private Set<TimeTableEntry> entrySet;

	public Room (String name, EntriesRepository entries, RoomsRepository rooms){
		//this.UsedMaterial = UsedMaterial;
		this.name = name;
		entrySet = new HashSet<>();
		rooms.save(this);
	}

	private Room (){
		//this.UsedMaterial = UsedMaterial;
		this.name = null;
	}

	//Getter
	/*public Material getUsedMaterial() {
		return UsedMaterial;
	}*/



	/*public int getRoomId() {
		return RoomId;
	}*/

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
