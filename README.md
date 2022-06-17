# "springtime" 
### Spring Boot Exercise Application

```
git clone git@github.com:jarrykinn/springtime.git
cd springtime
./mvnw package
./mvnw spring-boot:run
```

You can then access **springtime** here: http://localhost:8080/

Following endpoints are available:

* **POST /exchange_amount**
  * Exchange money from EUR to SEK and USD and vice versa
  * the result can be read from response `toAmount`
  * `date` and `timestamp` tells when the exchange rate has been updated
```
POST http://localhost:8080/exchange_amount?from=SEK&to=EUR&from_amount=10
    example response:
        {
            "fromCurrency": "SEK",
            "toCurrency": "EUR",
            "exchangeRate": 0.093353,
            "date": "2022-06-16",
            "timestamp": 1655403723,
            "fromAmount": 10.0,
            "toAmount": 0.93353003,
            "new": false
        }
```

* **POST /validate_ssn**
    * Validate Social Security Number
    * by the rules from here: https://dvv.fi/en/personal-identity-code
```
POST http://localhost:8080/validate_ssn?ssn=131052-308T&country_code=FI
    example response:
        {
        }
```



![image info](./README-images/cron-jobs-gcp.png)
