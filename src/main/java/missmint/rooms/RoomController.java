package missmint.rooms;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RoomController {

	@GetMapping("/rooms.html")
	@PreAuthorize("isAuthenticated()")
	public String index() {
		return "rooms";
	}

}
