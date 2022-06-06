package pl.pracainz.delegacje.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pracainz.delegacje.model.Delegation;
import pl.pracainz.delegacje.model.Expense;
import pl.pracainz.delegacje.model.Photo;
import pl.pracainz.delegacje.repository.ExpenseRepository;
import pl.pracainz.delegacje.repository.PhotoRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

@Service
public class ExpenseService {
    @Autowired
    ExpenseRepository expenseRepository;
    @Autowired
    CategoryService categoryService;
    @Autowired
    CurrencyService currencyService;
    @Autowired
    DelegationService delegationService;
    @Autowired
    PhotoRepository photoRepository;

    public Expense getById(int id) { return expenseRepository.findById(id); }

    public Expense saveExpense(Expense expense, int delegationId) {
            expense.setPrice(Double.parseDouble(expense.getPaymentTemp()));
            expense.setValueInPln(0);
            expense.setCategory(categoryService.getByName(expense.getCategoryNameTemp()));
            expense.setCurrency(currencyService.getByName(expense.getCurrencyNameTemp()));
            expense.setDelegation(delegationService.getById(delegationId));
            return expenseRepository.save(expense);
    }

    public List<Expense> saveAllExpenses(Set<Expense> expenses) {
        return expenseRepository.saveAll(expenses);
    }


}
