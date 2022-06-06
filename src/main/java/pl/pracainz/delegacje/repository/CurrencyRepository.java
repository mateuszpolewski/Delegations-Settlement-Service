package pl.pracainz.delegacje.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pracainz.delegacje.model.Category;
import pl.pracainz.delegacje.model.Currency;
import pl.pracainz.delegacje.model.Role;

import java.util.List;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    Currency findById(int id);
    Currency findByName(String name);
    List<Currency> findAll();

    void deleteById(int id);
}
