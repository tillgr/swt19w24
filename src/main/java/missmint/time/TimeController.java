package missmint.time;

import org.salespointframework.time.BusinessTime;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.Duration;

/**
 * Controller for the time forwarding.
 */
@Controller
public class TimeController {
	private final TimeService timeService;

	public TimeController(TimeService timeService) {
		Assert.notNull(timeService, "timeService should not be null");

		this.timeService = timeService;
	}

	/**
	 * Forward the time by one day.
	 * @return a redirect to <code>/</code>.
	 *
	 * @see TimeService#forward(Duration)
	 */
	@PostMapping("/forward")
	@PreAuthorize("isAuthenticated()")
	public String forward() {
		timeService.forward(Duration.ofDays(1));
		return "redirect:/";
	}
}
