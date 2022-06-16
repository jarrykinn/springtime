package springtime.ssn;

import com.fasterxml.jackson.annotation.JsonIgnore;
import springtime.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.validation.constraints.Pattern;

@Entity
public class PersonalIdentityCode extends BaseEntity {

	/**
	 * Just to hide unnecessary ID from response
	 * @return
	 */
	@JsonIgnore
	@Override
	public Integer getId() {
		return super.getId();
	}

	/*
	 * @Pattern( regexp =
	 * "^(0[1-9]|[12]\\d|3[01])(0[1-9]|1[0-2])([5-9]\\d\\+|\\d\\d-|[01]\\dA)\\d{3}[\\dABCDEFHJKLMNPRSTUVWXY]$",
	 * flags = Pattern.Flag.UNICODE_CASE)
	 */
	@SsnConstraint
	private String ssn;

	@Pattern(regexp = "^[\\D]{2}$", flags = Pattern.Flag.UNICODE_CASE)
	private String countryCode;

	@Transient
	private boolean ssn_valid;

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public boolean isSsn_valid() {
		return ssn_valid;
	}

	public void setSsn_valid(boolean ssn_valid) {
		this.ssn_valid = ssn_valid;
	}

}
