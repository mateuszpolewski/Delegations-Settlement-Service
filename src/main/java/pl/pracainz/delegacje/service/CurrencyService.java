package pl.pracainz.delegacje.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pracainz.delegacje.model.Category;
import pl.pracainz.delegacje.model.Currency;
import pl.pracainz.delegacje.repository.CurrencyRepository;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.convert.CurrencyConversion;
import javax.money.convert.MonetaryConversions;
import java.util.List;

@Service
public class CurrencyService {
    @Autowired
    CurrencyRepository currencyRepository;

    public Currency getByName(String name) { return currencyRepository.findByName(name); }
    public List<Currency> getAll() {
        return currencyRepository.findAll();
    }


    public double convertCurrencies(double amount, String currencyFrom, String currencyTo) {

		MonetaryAmount moneyCurrencyFrom = Monetary.getDefaultAmountFactory().setCurrency(currencyFrom)
				.setNumber(amount).create();

		CurrencyConversion conversionCurrencyTo = MonetaryConversions.getConversion(currencyTo);
		MonetaryAmount convertedAmountOfMoney = moneyCurrencyFrom.with(conversionCurrencyTo);

		return convertedAmountOfMoney.getNumber().doubleValue();

    }
}
