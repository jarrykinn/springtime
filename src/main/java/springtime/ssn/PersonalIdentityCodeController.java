package springtime.ssn;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import springtime.exhangerates.ExchangeRate;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@RestController
class PersonalIdentityCodeController {

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@PostMapping("/validate_ssn/body")
	public ResponseEntity validateSsnBody(@RequestBody @Valid PersonalIdentityCode ssn) {
		System.out.println("SSN: " + ssn.getSsn());
		ssn.setSsn_valid(true);
		return ResponseEntity.ok().body(ssn);
	}

	@PostMapping("/validate_ssn")
	public ResponseEntity validateSsnParams(@RequestParam String ssn, @RequestParam String country_code) {
		return ResponseEntity.ok().build();
	}

	private ResponseEntity respondError(HttpStatus status, String message) {
		Map<String, String> error = new HashMap<>();
		error.put("message", message);
		return ResponseEntity.status(status).body(error);
	}

}