package missmint.time;

import org.salespointframework.time.BusinessTime;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.Duration;

@Controller
public class TimeController {
	private final TimeService timeService;

	public TimeController(TimeService timeService) {
		this.timeService = timeService;
	}

	@PostMapping("/forward")
	@PreAuthorize("isAuthenticated()")
	public String forward() {
		timeService.forward(Duration.ofDays(1));
		return "redirect:/";
	}
}
