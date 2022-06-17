package springtime.ssn;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ValidationErrorResponse {

	private boolean ssn_valid;

	@JsonProperty("errors")
	private List<Violation> violations = new ArrayList<>();

	public List<Violation> getViolations() {
		return violations;
	}

	public boolean isSsn_valid() {
		return ssn_valid;
	}

	public void setSsn_valid(boolean ssn_valid) {
		this.ssn_valid = ssn_valid;
	}

}
