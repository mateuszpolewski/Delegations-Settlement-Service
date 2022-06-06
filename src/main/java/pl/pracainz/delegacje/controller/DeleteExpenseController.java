package pl.pracainz.delegacje.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pl.pracainz.delegacje.model.Delegation;
import pl.pracainz.delegacje.model.Expense;
import pl.pracainz.delegacje.model.Photo;
import pl.pracainz.delegacje.model.User;
import pl.pracainz.delegacje.repository.DelegationRepository;
import pl.pracainz.delegacje.repository.ExpenseRepository;
import pl.pracainz.delegacje.repository.PhotoRepository;
import pl.pracainz.delegacje.repository.UserRepository;
import pl.pracainz.delegacje.service.DelegationService;
import pl.pracainz.delegacje.service.DelegationStatusService;
import pl.pracainz.delegacje.service.ExpenseService;
import pl.pracainz.delegacje.service.UserService;

import java.util.ArrayList;
import java.util.Set;


@Controller
public class DeleteExpenseController {

    @Autowired
    ExpenseService expenseService;
    @Autowired
    PhotoRepository photoRepository;
    @Autowired
    ExpenseRepository expenseRepository;

    @Transactional
    @RequestMapping(value = "/user/delegation/{id}/expenses/delete-expense/{expenseId}", method = RequestMethod.GET)
    public ModelAndView deleteExpense(@PathVariable("id") int id, @PathVariable("expenseId") int expenseId , RedirectAttributes redirectAttributes) {
        Expense expense = expenseService.getById(expenseId);
        if(!expense.getPhotos().isEmpty())
            for(Photo photo : expense.getPhotos())
            {
                photoRepository.deleteById(photo.getId());
            }
        expenseRepository.deleteById(expenseId);

        redirectAttributes.addFlashAttribute("successMessage", "Usunięto koszt.");

        return new ModelAndView("redirect:/auth/delegation/" + id + "/expenses");

        //return new RedirectView("/auth/delegation/" + id + "/expenses");
    }

}
