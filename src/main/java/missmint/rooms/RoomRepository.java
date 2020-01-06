package missmint.rooms;

import org.springframework.data.repository.CrudRepository;

import java.util.stream.Stream;

/**
 * repository which stores the rooms
 */
public interface RoomRepository extends CrudRepository<Room, Long> {

	public Stream<Room> findByName(String name);

}
