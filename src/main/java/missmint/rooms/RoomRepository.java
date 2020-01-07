package missmint.rooms;

import org.springframework.data.repository.CrudRepository;

/**
 * repository which stores the rooms
 */
public interface RoomRepository extends CrudRepository<Room, Long> {
}
