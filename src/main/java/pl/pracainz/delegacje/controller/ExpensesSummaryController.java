package pl.pracainz.delegacje.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import pl.pracainz.delegacje.model.Delegation;
import pl.pracainz.delegacje.model.Expense;
import pl.pracainz.delegacje.repository.DelegationRepository;
import pl.pracainz.delegacje.service.DelegationService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;


@Controller
public class ExpensesSummaryController {

    @Autowired
    DelegationService delegationService;
    @Autowired
    DelegationRepository delegationRepository;

    @RequestMapping(value="/accountant/delegation/{id}/expenses-summary", method = RequestMethod.GET)
    public ModelAndView showExpenses(ModelAndView modelAndView, @PathVariable("id") int delegationId){

        List<Expense> mainList = new ArrayList<Expense>();
        Delegation delegation = delegationService.getById(delegationId);
        mainList.addAll(delegation.getExpenses());
        Collections.sort(mainList, (d1, d2) -> {
            return d2.getId() - d1.getId();
        });

        modelAndView.setViewName("/accountant/expense-summary");
        modelAndView.addObject("delegationId", delegationId);
        modelAndView.addObject("delegation", delegation);
        modelAndView.addObject("expenses",mainList );
        return modelAndView;
    }
    @RequestMapping(value="/accountant/delegation/{id}/expenses-summary", method = RequestMethod.POST)
    public ModelAndView showExpensesAndChangeAdvPayment(ModelAndView modelAndView, @PathVariable("id") int delegationId){
        List<Expense> mainList = new ArrayList<Expense>();
        Delegation delegation = delegationService.getById(delegationId);
        mainList.addAll(delegation.getExpenses());
        Collections.sort(mainList, (d1, d2) -> {
            return d2.getId() - d1.getId();
        });
        modelAndView.setViewName("/accountant/expense-summary");
        modelAndView.addObject("delegationId", delegationId);
        modelAndView.addObject("delegation", delegation);
        modelAndView.addObject("expenses", mainList);
        return modelAndView;
    }
}
