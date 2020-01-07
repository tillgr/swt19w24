package missmint;

import org.salespointframework.time.BusinessTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Controller
public class IndexController {
	private final BusinessTime time;

	public IndexController(BusinessTime time) {
		this.time = time;
	}

	@GetMapping("/")
	public String index(Model model, Locale locale) {
		model.addAttribute("time", time.getTime().format(DateTimeFormatter.ISO_LOCAL_DATE.localizedBy(locale)));
		return "index";
	}
}