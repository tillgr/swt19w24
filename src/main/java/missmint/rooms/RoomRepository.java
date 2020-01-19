package missmint.rooms;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Repository which stores the rooms
 */
public interface RoomRepository extends CrudRepository<Room, Long> {

	/**
	 *
	 * @param name of the room, which is searched for
	 * @return boolean value
	 */
	public boolean existsByName(String name);

}
