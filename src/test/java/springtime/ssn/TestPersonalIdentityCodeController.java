package springtime.ssn;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(PersonalIdentityCodeController.class)
class TestPersonalIdentityCodeController {

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	void setup() {
	}

	@Test
	public void validateSsnBody() throws Exception {
		System.out.println("validateSsnBody()");
		String validSsn = "131052-308T";
		String malformedSsn = "131052Ä308T";
		String wrongChecksum = "131052-309T";
		String validCountryCode = "FI";
		String notValidCountryCode = "SE";

		ObjectMapper mapper = new ObjectMapper();
		PersonalIdentityCode ssn = new PersonalIdentityCode();

		ssn.setSsn(validSsn);
		ssn.setCountryCode(validCountryCode);
		// OK SSN
		mockMvc.perform(MockMvcRequestBuilders.post("/validate_ssn").content(mapper.writeValueAsString(ssn))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(content().json("{'ssn_valid': true}"));

		// Malformed SSN
		ssn.setSsn(malformedSsn);
		ssn.setCountryCode(validCountryCode);
		mockMvc.perform(MockMvcRequestBuilders.post("/validate_ssn").content(mapper.writeValueAsString(ssn))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(content().json("{'ssn_valid': false}"));

		// Wrong checksum SSN
		ssn.setSsn(wrongChecksum);
		ssn.setCountryCode(validCountryCode);
		mockMvc.perform(MockMvcRequestBuilders.post("/validate_ssn").content(mapper.writeValueAsString(ssn))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(content().json("{'ssn_valid': false}"))
				.andExpect(content().json("{'errors': [{'message': 'SSN checksum character mismatch T ≠ U'}]}"));

		// Not valid country code
		ssn.setSsn(validSsn);
		ssn.setCountryCode(notValidCountryCode);
		mockMvc.perform(MockMvcRequestBuilders.post("/validate_ssn").content(mapper.writeValueAsString(ssn))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(content().json("{'ssn_valid': false}"))
				.andExpect(content().json("{'errors': [{'rejected_value': 'SE'}]}"));

		System.out.flush();
	}

}