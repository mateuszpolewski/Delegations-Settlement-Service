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
import pl.pracainz.delegacje.service.CategoryService;
import pl.pracainz.delegacje.service.CurrencyService;
import pl.pracainz.delegacje.service.ExpenseService;

import javax.mail.MessagingException;
import javax.validation.Valid;

@Controller
public class AddExpenseController {
    @Autowired
    ExpenseService expenseService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    CurrencyService currencyService;

    @RequestMapping(value="/user/delegation/{id}/expenses/add-expense", method = RequestMethod.GET)
    public ModelAndView newRegistrationForm(ModelAndView modelAndView, @PathVariable("id") int delegationId ){
        modelAndView.addObject("expense", new Expense());
        modelAndView.addObject("categories", categoryService.getAll());
        modelAndView.addObject("currencies", currencyService.getAll());
        modelAndView.addObject("delegationId", delegationId);
        modelAndView.setViewName("/user/add-expense");
        return modelAndView;
    }
    @RequestMapping(value="/user/delegation/{id}/expenses/add-expense", method = RequestMethod.POST)
    public ModelAndView registerUserInDatabase(ModelAndView modelAndView, @PathVariable("id") int delegationId,
                                               @Valid Expense expense, BindingResult bindingResult) {
        if(!bindingResult.hasErrors()) {
            expenseService.saveExpense(expense, delegationId);
            modelAndView.addObject("expense", new Expense());
            modelAndView.addObject("successMessage", "Poprawnie dodano koszt.");
        }
        modelAndView.setViewName("/user/add-expense");
        modelAndView.addObject("categories", categoryService.getAll());
        modelAndView.addObject("currencies", currencyService.getAll());
        modelAndView.addObject("delegationId", delegationId);
        return modelAndView;
    }
}
