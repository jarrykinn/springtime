package springtime.exhangerates;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 */
public interface ExchangeRateRepository extends Repository<ExchangeRate, Integer> {

	@Query("SELECT DISTINCT rates FROM ExchangeRate rates WHERE rates.fromCurrency LIKE :from_currency% ")
	@Transactional(readOnly = true)
	ExchangeRate findByFromCurrency(@Param("from_currency") String fromCurrency);

	@Query("SELECT DISTINCT rates FROM ExchangeRate rates WHERE rates.fromCurrency LIKE :from_currency% AND rates.toCurrency LIKE :to_currency%")
	@Transactional(readOnly = true)
	ExchangeRate findByFromCurrencyAndToCurrency(@Param("from_currency") String fromCurrency,
			@Param("to_currency") String toCurrency);

	@Query("SELECT rates FROM ExchangeRate rates WHERE rates.id =:id")
	@Transactional(readOnly = true)
	ExchangeRate findById(@Param("id") Integer id);

	void save(ExchangeRate rate);

	@Query("SELECT rates FROM ExchangeRate rates")
	@Transactional(readOnly = true)
	Page<ExchangeRate> findAll(Pageable pageable);

}
