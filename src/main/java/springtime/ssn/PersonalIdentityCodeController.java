package springtime.ssn;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller to expose endpoint for validating Social Security Number(s) SSN.
 * 		POST /validate_ssn
 *         {
 *           "ssn": "131052-308T",
 *           "countryCode": "FI"
 *         }
 */
@RestController
class PersonalIdentityCodeController {

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@PostMapping("/validate_ssn")
	public ResponseEntity validateSsnBody(@RequestBody @Valid PersonalIdentityCode ssn) {
		System.out.println("SSN: " + ssn.getSsn());
		ssn.setSsn_valid(true);
		return ResponseEntity.ok().body(ssn);
	}

	private ResponseEntity respondError(HttpStatus status, String message) {
		Map<String, String> error = new HashMap<>();
		error.put("message", message);
		return ResponseEntity.status(status).body(error);
	}

}