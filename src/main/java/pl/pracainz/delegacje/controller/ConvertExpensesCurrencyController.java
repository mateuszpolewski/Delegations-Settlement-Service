package pl.pracainz.delegacje.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pl.pracainz.delegacje.model.Expense;
import pl.pracainz.delegacje.model.User;
import pl.pracainz.delegacje.service.CurrencyService;
import pl.pracainz.delegacje.service.DelegationService;
import pl.pracainz.delegacje.service.ExpenseService;

import java.util.HashSet;

@Controller
public class ConvertExpensesCurrencyController {
    @Autowired
    CurrencyService currencyService;
    @Autowired
    DelegationService delegationService;
    @Autowired
    ExpenseService expenseService;

    @RequestMapping(value = "/accountant/delegation/{id}/expenses-summary/convert", method = RequestMethod.GET)
    public ModelAndView convertCurrenciesInExpenses(@PathVariable int id, RedirectAttributes redirectAttributes) {
        HashSet<Expense> expenses = new HashSet<>();
        expenses.addAll(delegationService.getById(id).getExpenses());
        double amountOfPln;
        for(Expense expense : expenses) {
            if (expense.getCurrency().getName() != "PLN") {
                amountOfPln = currencyService.convertCurrencies(expense.getPrice(), expense.getCurrency().getName(), "PLN");
                expense.setValueInPln(Math.round(amountOfPln  * 100.0) / 100.0);
            } else
                expense.setValueInPln(expense.getPrice());
        }

        redirectAttributes.addFlashAttribute("successMessage", "Zamieniono waluty");

        expenseService.saveAllExpenses(expenses);
        return new ModelAndView("redirect:/accountant/delegation/{id}/expenses-summary");
    }
}
