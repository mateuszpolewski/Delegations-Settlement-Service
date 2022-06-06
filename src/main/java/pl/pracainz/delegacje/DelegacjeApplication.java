package pl.pracainz.delegacje;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.convert.CurrencyConversion;
import javax.money.convert.MonetaryConversions;

@SpringBootApplication
//@EnableAutoConfiguration
//@ComponentScan(basePackages="pl.pracainz.delegacje")
//@EnableJpaRepositories("pl.pracainz.delegacje.repository")
public class DelegacjeApplication {

	public static void main(String[] args) {
		SpringApplication.run(DelegacjeApplication.class, args);

	}

}
