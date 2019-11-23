package missmint.time;

import org.springframework.stereotype.Service;

@Service
public class TimeService {
	/**
	 * This function is called whenever the time is forwarded to process time dependant events.
	 */
	public void onForward() {
		// TODO process orders
		// TODO pay staff
	}
}
