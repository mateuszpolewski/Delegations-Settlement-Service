package pl.pracainz.delegacje.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import pl.pracainz.delegacje.model.Delegation;
import pl.pracainz.delegacje.model.Expense;
import pl.pracainz.delegacje.model.User;
import pl.pracainz.delegacje.repository.ExpenseRepository;
import pl.pracainz.delegacje.service.CategoryService;
import pl.pracainz.delegacje.service.CurrencyService;
import pl.pracainz.delegacje.service.ExpenseService;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class EditExpenseController {
    @Autowired
    ExpenseRepository expenseRepository;
    @Autowired
    ExpenseService expenseService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    CurrencyService currencyService;

    @RequestMapping(value="/auth/delegation/{id}/expenses/edit-expense/{expenseId}", method = RequestMethod.GET)
    public ModelAndView newRegistrationForm(ModelAndView modelAndView, @PathVariable("id") int delegationId, @PathVariable("expenseId") int expenseId ){
        Expense expense = expenseService.getById(expenseId);
        modelAndView.addObject("expense", expense);
        expense.setPaymentTemp(Double.toString(expense.getPrice()));
        expense.setCategoryNameTemp(expense.getCategory().getName());
        expense.setCurrencyNameTemp(expense.getCurrency().getName());
        modelAndView.addObject("categories", categoryService.getAll());
        modelAndView.addObject("currencies", currencyService.getAll());
        modelAndView.addObject("delegationId", delegationId);
        modelAndView.addObject("expenseId", expenseId);
        modelAndView.setViewName("/auth/edit-expense");

        return modelAndView;
    }
    @RequestMapping(value="/auth/delegation/{id}/expenses/edit-expense/{expenseId}", method = RequestMethod.POST)
    public ModelAndView registerUserInDatabase(ModelAndView modelAndView, @PathVariable("id") int delegationId, @PathVariable("expenseId") int expenseId,
                                               @Valid Expense expense, BindingResult bindingResult) {
        Expense expenseToUpdate = expenseService.getById(expenseId);
        if(!bindingResult.hasErrors()) {
            expenseToUpdate = getUpdatedExpense(expenseToUpdate, expense);
            expenseRepository.save(expenseToUpdate);
            modelAndView.addObject("successMessage", "Poprawnie edytowano koszt.");
            expense = expenseToUpdate;
        }
        modelAndView.setViewName("/auth/edit-expense");
        modelAndView.addObject("categories", categoryService.getAll());
        modelAndView.addObject("currencies", currencyService.getAll());
        modelAndView.addObject("delegationId", delegationId);
        modelAndView.addObject("expenseId", expenseId);
        modelAndView.addObject("expense", expense);
        return modelAndView;
    }
    private Expense getUpdatedExpense(Expense expenseToUpdate, Expense expense) {
        expenseToUpdate.setName(expense.getName());
        expenseToUpdate.setDescription(expense.getDescription());
        expenseToUpdate.setPaymentTemp(expense.getPaymentTemp());
        expenseToUpdate.setCategoryNameTemp(expense.getCategoryNameTemp());
        expenseToUpdate.setCurrencyNameTemp(expense.getCurrencyNameTemp());
        expenseToUpdate.setPrice(Double.parseDouble(expenseToUpdate.getPaymentTemp()));
        expenseToUpdate.setCategory(categoryService.getByName(expenseToUpdate.getCategoryNameTemp()));
        expenseToUpdate.setCurrency(currencyService.getByName(expenseToUpdate.getCurrencyNameTemp()));
        return expenseToUpdate;
    }
}
