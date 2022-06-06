package pl.pracainz.delegacje.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pl.pracainz.delegacje.model.Delegation;
import pl.pracainz.delegacje.model.Expense;
import pl.pracainz.delegacje.repository.DelegationRepository;
import pl.pracainz.delegacje.service.DelegationService;
import pl.pracainz.delegacje.service.DelegationStatusService;
import pl.pracainz.delegacje.service.PDFGenerator;
import pl.pracainz.delegacje.service.UserService;

import java.io.FileNotFoundException;
import java.util.HashSet;

@Controller
public class SumDelegationCostController {
    @Autowired
    DelegationService delegationService;
    @Autowired
    DelegationRepository delegationRepository;
    @Autowired
    UserService userService;

    @Autowired
    DelegationStatusService delegationStatusService;

    @RequestMapping(value = "/accountant/delegation/{id}/expenses-summary/sum-up", method = RequestMethod.GET)
    public ModelAndView sumExpensesInDelegation(@PathVariable int id, RedirectAttributes redirectAttributes){
        Delegation delegation = delegationService.getById(id);
        HashSet<Expense> expenses = new HashSet<>();
        expenses.addAll(delegation.getExpenses());
        double sum = 0;
        for(Expense expense : expenses) {
            sum += expense.getValueInPln();
        }
        //sum += delegation.getAdvancePaymentIssued();
        sum -= delegation.getAdvancedPayment();

        delegation.setSummaryCost(Math.round(sum  * 100.0) / 100.0);
        delegation.setAccountantId(userService.getLoggedInUser().getId());
        delegation.setDelegationStatus(delegationStatusService.getByStatus("SETTLED"));

        redirectAttributes.addFlashAttribute("successMessage", "Podsumowano koszty");

        delegationRepository.save(delegation);
        return new ModelAndView("redirect:/accountant/delegation/{id}/expenses-summary");
    }
}
