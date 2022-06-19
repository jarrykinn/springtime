package springtime.exchangerates;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExchangeRateController.class)
class TestExchangeRateController {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ExchangeRateRepository rates;

	@BeforeEach
	void setup() {
		ExchangeRate rateEurToSek = new ExchangeRate();
		rateEurToSek.setFromCurrency("EUR");
		rateEurToSek.setToCurrency("SEK");
		rateEurToSek.setDate(LocalDate.now());
		rateEurToSek.setTimestamp(new Date().getTime());
		rateEurToSek.setExchangeRate(new BigDecimal(10.712079));
		given(this.rates.findAll(any(Pageable.class)))
				.willReturn(new PageImpl<ExchangeRate>(Lists.newArrayList(rateEurToSek)));
		given(this.rates.findByFromCurrencyAndToCurrency("EUR", "SEK")).willReturn(rateEurToSek);
		// rates.save(rateEurToSek);
	}

	@Test
	public void testRepository() {
		System.out.println("testRepository()");
		Pageable pageable = PageRequest.of(0, 10);
		Page<ExchangeRate> rs = rates.findAll(pageable);
		assertEquals(1, rs.getTotalElements());
		System.out.flush();
	}

	@Test
	public void testGetRates() throws Exception {
		System.out.println("testGetRates()");
		mockMvc.perform(get("/rates")).andExpect(status().isOk())
				.andExpect(content().json("{'content':[{'to_currency':'SEK'}]}"));
		mockMvc.perform(get("/rates")).andExpect(status().isOk()).andExpect(
				content().json("{'content':[{'exchange_rate':10.712078999999999240344550344161689281463623046875}]}"));
		System.out.flush();
	}

	@Test
	public void testExchangeAmount() throws Exception {
		System.out.println("testExchangeAmount()");
		mockMvc.perform(get("/exchange_amount?from=EUR&to=SEK&from_amount=10")).andExpect(status().isOk())
				.andExpect(content().json("{'to_amount':107.12079}"));
		System.out.flush();
	}

}