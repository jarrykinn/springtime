package springtime.ssn;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Violation {

	public Violation(String fieldName, String rejectedValue, String message) {
		this.fieldName = fieldName;
		this.rejectedValue = rejectedValue;
		this.message = message;
	}

	@JsonProperty("field_name")
	private final String fieldName;

	@JsonProperty("rejected_value")
	private final String rejectedValue;

	@JsonProperty("message")
	private final String message;

	public String getFieldName() {
		return fieldName;
	}

	public String getRejectedValue() {
		return rejectedValue;
	}

	public String getMessage() {
		return message;
	}

}
