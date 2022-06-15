package springtime.exhangerates;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

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
	public ExchangeRate findOwner(@PathVariable(name = "id", required = false) Integer id) {
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

	@PostMapping("/rates")
	public ResponseEntity processCreateExchangeRate(@RequestBody ExchangeRate exchangeRate) {
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