package springtime.ssn;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CountryCodeConstraintValidator implements ConstraintValidator<CountryCodeConstraint, String> {

	public boolean isValid(String countryCode, ConstraintValidatorContext cvc) {
		Pattern pattern = Pattern.compile("^[A-Z]{2}$");
		if (countryCode == null) {
			ValidatorTools.buildErrorMessage("Missing countryCode!", cvc);
			return false;
		}
		countryCode = countryCode.trim();
		if (countryCode.length() <= 0) {
			ValidatorTools.buildErrorMessage("Empty countryCode!", cvc);
			return false;
		}
		Matcher matcher = pattern.matcher(countryCode);
		try {
			if (!matcher.matches()) {
				String nearCharacter = countryCode.substring(ValidatorTools.indexOfLastMatch(pattern, countryCode));
				ValidatorTools.buildErrorMessage("Malformed countryCode near: '" + nearCharacter + "'", cvc);
				return false;
			}
			else {
				if (countryCode.equals("FI"))
					return true;
				return false;
			}
		}
		catch (Exception e) {
			ValidatorTools.buildErrorMessage(
					"CountryCodeConstraintValidator: " + e.getClass().getName() + "; " + e.getMessage(), cvc);
			return false;
		}
	}

}
