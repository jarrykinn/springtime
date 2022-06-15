package springtime.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
class WelcomeController {

	@GetMapping("/")
	public Map welcome() {
		Map response = new HashMap();
		response.put("welcome", "Welcome to the SpringTime endpoints");
		List<String> endpoints = new ArrayList<>();
		endpoints.add("POST /validate_ssn");
		endpoints.add("GET /exchange_amount");
		response.put("endpoints", endpoints);
		return response;
	}

}
