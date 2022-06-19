package springtime.exchangerates;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 *
 */
@RestController
class ExchangeRateController {

	private final ExchangeRateRepository ratesRepository;

	public ExchangeRateController(ExchangeRateRepository ratesService) {
		this.ratesRepository = ratesService;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@ModelAttribute("rate")
	public ExchangeRate findExchangeRate(@PathVariable(name = "id", required = false) Integer id) {
		return id == null ? new ExchangeRate() : this.ratesRepository.findById(id);
	}

	@GetMapping("/rates")
	@ResponseBody
	public Page<ExchangeRate> processGetAllRates() {
		int pageSize = 10;
		int page = 1; // hard coded so far
		Pageable pageable = PageRequest.of(page - 1, pageSize);
		return ratesRepository.findAll(pageable);
	}

	@GetMapping("/rates/update")
	public ResponseEntity createOrUpdateExchangeRate(
			@RequestHeader(value = "X-Appengine-Cron", required = false) String headerXAppengineCron) {
		Map<String, Object> result = new HashMap();
		System.out.println("CronJob called! with X-Appengine-Cron: " + headerXAppengineCron);
		String uriEurToSekUsd = "https://api.apilayer.com/exchangerates_data/latest?symbols=SEK%2CUSD&base=EUR";
		String uriSekToUsd = "https://api.apilayer.com/exchangerates_data/latest?symbols=USD&base=SEK";
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("apikey", "COX9LGyhShxz2EyEaJHJcGd7QShikAx1");
		HttpEntity<String> entity = new HttpEntity<>("body", headers);

		// base EUR to SEK and USD
		ResponseEntity<Map> response = restTemplate.exchange(uriEurToSekUsd, HttpMethod.GET, entity, Map.class);
		long timestamp = Integer.toUnsignedLong((Integer) response.getBody().get("timestamp"));
		// LocalDate date = LocalDate.parse(response.getBody().get("date").toString()));
		Map latestRates = (Map) response.getBody().get("rates");
		BigDecimal fromEurToSek = BigDecimal.valueOf((Double) latestRates.get("SEK"));
		BigDecimal fromEurToUsd = BigDecimal.valueOf((Double) latestRates.get("USD"));
		updateOrCreateExchangeRate("EUR", "SEK", fromEurToSek, timestamp, result);
		updateOrCreateExchangeRate("SEK", "EUR", new BigDecimal(1.0 / fromEurToSek.doubleValue()), timestamp, result);
		updateOrCreateExchangeRate("EUR", "USD", fromEurToUsd, timestamp, result);
		updateOrCreateExchangeRate("USD", "EUR", new BigDecimal(1.0 / fromEurToUsd.doubleValue()), timestamp, result);

		// base SEK to USD
		response = restTemplate.exchange(uriSekToUsd, HttpMethod.GET, entity, Map.class);
		timestamp = Integer.toUnsignedLong((Integer) response.getBody().get("timestamp"));
		latestRates = (Map) response.getBody().get("rates");
		BigDecimal fromSekToUsd = BigDecimal.valueOf((Double) latestRates.get("USD"));
		updateOrCreateExchangeRate("SEK", "USD", fromSekToUsd, timestamp, result);
		updateOrCreateExchangeRate("USD", "SEK", new BigDecimal(1.0 / fromSekToUsd.doubleValue()), timestamp, result);

		return ResponseEntity.ok().body(result);
	}

	private ExchangeRate updateOrCreateExchangeRate(String from, String to, BigDecimal exchangeRate, long timestamp,
			Map result) {
		ExchangeRate rate = new ExchangeRate();
		rate.setTimestamp(timestamp);
		rate.setDate(LocalDate.now());
		rate.setFromCurrency(from);
		rate.setToCurrency(to);
		rate.setExchangeRate(exchangeRate);
		rate = (ExchangeRate) createOrUpdateExchangeRate(rate).getBody();
		result.put(from + " > " + to, exchangeRate);
		return rate;
	}

	private ResponseEntity createOrUpdateExchangeRate(@RequestBody ExchangeRate exchangeRate) {
		if (exchangeRate.getFromCurrency() == null || exchangeRate.getToCurrency() == null) {
			return respondError(HttpStatus.BAD_REQUEST, "From or To Currency missing!");
		}
		ExchangeRate rate = ratesRepository.findByFromCurrencyAndToCurrency(exchangeRate.getFromCurrency(),
				exchangeRate.getToCurrency());
		if (rate != null) {
			exchangeRate.setId(rate.getId());
		}
		try {
			exchangeRate.setFromCurrency(exchangeRate.getFromCurrency().toUpperCase(Locale.ROOT));
			exchangeRate.setToCurrency(exchangeRate.getToCurrency().toUpperCase(Locale.ROOT));
			this.ratesRepository.save(exchangeRate);
		}
		catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
			return respondError(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		System.out.println("Updated exchange rate, " + exchangeRate.getFromCurrency() + " > "
				+ exchangeRate.getToCurrency() + " rate: " + exchangeRate.getExchangeRate());
		return ResponseEntity.ok(ratesRepository.findById(exchangeRate.getId()));
	}

	@GetMapping("/exchange_amount")
	public ResponseEntity exchangeAmount(@RequestParam String from, @RequestParam String to,
			@RequestParam Float from_amount) {
		System.out.println("from: " + from);
		System.out.println("to: " + to);
		System.out.println("fromAmount: " + from_amount);
		ExchangeRate exchangeRate = ratesRepository.findByFromCurrencyAndToCurrency(from, to);
		exchangeRate.setFromAmount(from_amount);
		exchangeRate.setToAmount(exchangeRate.getExchangeRate().floatValue() * from_amount);
		return ResponseEntity.ok(exchangeRate);
	}

	@GetMapping("/rates/{rateId}")
	public ExchangeRate getExchangeRate(@PathVariable("rateId") int rateId) {
		return this.ratesRepository.findById(rateId);
	}

	private ResponseEntity respondError(HttpStatus status, String message) {
		Map<String, String> error = new HashMap<>();
		error.put("message", message);
		return ResponseEntity.status(status).body(error);
	}

}