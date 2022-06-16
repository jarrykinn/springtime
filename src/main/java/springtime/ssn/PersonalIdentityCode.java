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

	@SsnConstraint
	private String ssn;

	@CountryCodeConstraint
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
