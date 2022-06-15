package springtime.exhangerates;

import org.springframework.format.annotation.DateTimeFormat;
import springtime.model.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Business object representing an exchange rate.
 */
@Entity
@Table(name = "rates")
public class ExchangeRate extends BaseEntity {

	@Column(name = "from_currency")
	@NotEmpty
	private String fromCurrency;

	@Column(name = "full_name")
	private String fullName;

	@Column(name = "to_currency")
	@NotEmpty
	private String toCurrency;

	@Column(name = "exchange_rate", precision = 10, scale = 6)
	@DecimalMin(value = "0.0", inclusive = true)
	@DecimalMax(value = "9999999.9", inclusive = true)
	private BigDecimal exchangeRate;

	@Column(name = "date")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;

	@Column(name = "timestamp")
	private Long timestamp;

	@Column(name = "from_amount")
	@Transient
	private Float fromAmount;

	@Column(name = "to_amount")
	@Transient
	private Float toAmount;

	public String getFromCurrency() {
		return fromCurrency;
	}

	public void setFromCurrency(String fromCurrency) {
		this.fromCurrency = fromCurrency;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getToCurrency() {
		return toCurrency;
	}

	public void setToCurrency(String toCurrency) {
		this.toCurrency = toCurrency;
	}

	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public Float getFromAmount() {
		return fromAmount;
	}

	public void setFromAmount(Float fromAmount) {
		this.fromAmount = fromAmount;
	}

	public Float getToAmount() {
		return toAmount;
	}

	public void setToAmount(Float toAmount) {
		this.toAmount = toAmount;
	}

}
