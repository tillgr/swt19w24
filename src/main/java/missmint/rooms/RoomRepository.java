package missmint.rooms;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * repository which stores the rooms
 */
public interface RoomRepository extends CrudRepository<Room, Long> {

}
