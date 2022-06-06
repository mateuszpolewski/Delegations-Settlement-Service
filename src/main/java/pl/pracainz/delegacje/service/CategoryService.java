package pl.pracainz.delegacje.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pracainz.delegacje.model.Category;
import pl.pracainz.delegacje.model.Currency;
import pl.pracainz.delegacje.model.DelegationStatus;
import pl.pracainz.delegacje.model.Role;
import pl.pracainz.delegacje.repository.CategoryRepository;
import pl.pracainz.delegacje.repository.CurrencyRepository;
import pl.pracainz.delegacje.repository.DelegationRepository;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    public Category getByName(String name) { return categoryRepository.findByName(name); }
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }
}
