package springtime.ssn;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SsnConstraintValidator implements ConstraintValidator<SsnConstraint, String> {

	static final char[] controlChar = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
			'H', 'J', 'K', 'L', 'M', 'N', 'P', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y' };

	public boolean isValid(String ssn, ConstraintValidatorContext cvc) {
		// https://www.regexlib.com/UserPatterns.aspx?authorid=deeee49a-e0ad-418a-a4d4-9631160fdb35
		Pattern pattern = Pattern.compile(
				"^(0[1-9]|[12]\\d|3[01])(0[1-9]|1[0-2])([5-9]\\d\\+|\\d\\d-|[01]\\dA)\\d{3}[\\dABCDEFHJKLMNPRSTUVWXY]$");
		Matcher matcher = pattern.matcher(ssn);
		try {
			if (!matcher.matches()) {
				System.out.println("regex failed for: " + ssn);
				return false;
			}
			else {
				// nine-digit number person’s date of birth and individual number
				StringBuffer nineDigits = new StringBuffer();
				nineDigits.append(ssn.substring(0, 6)); // birthday digits
				nineDigits.append(ssn.substring(7, 10)); // individual number
				int nineDigitsNumber = Integer.valueOf(nineDigits.toString());
				// the control character is determined on the basis of the remainder
				// according to the table
				int reminder = nineDigitsNumber % 31;
				// given control character
				char ssnControlChar = ssn.charAt(10);
				System.out.println("reminder: " + reminder);
				System.out.println("SSN control char: " + ssnControlChar);
				System.out.println("controlChar[reminder]: " + controlChar[reminder]);
				if (controlChar[reminder] != ssnControlChar) {
					System.out.println("controlChar failed " + ssnControlChar + " ≠ " + controlChar[reminder]);
					return false;
				}
				return true;
			}
		}
		catch (Exception e) {
			System.out.println(e.getClass().getName());
			System.out.println("SsnConstraintValidator: " + e.getClass().getName() + "; " + e.getMessage());
			return false;
		}
	}

}
