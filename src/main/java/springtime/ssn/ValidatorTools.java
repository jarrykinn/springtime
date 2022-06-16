package springtime.ssn;

import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorTools {

	public static int indexOfLastMatch(Pattern pattern, String input) {
		Matcher matcher = pattern.matcher(input);
		for (int i = input.length(); i > 0; --i) {
			Matcher region = matcher.region(0, i);
			if (region.matches() || region.hitEnd()) {
				return i;
			}
		}
		return 0;
	}

	public static void buildErrorMessage(String message, ConstraintValidatorContext cvc) {
		System.out.println(message);
		cvc.disableDefaultConstraintViolation();
		cvc.buildConstraintViolationWithTemplate(message).addConstraintViolation();
	}

}
