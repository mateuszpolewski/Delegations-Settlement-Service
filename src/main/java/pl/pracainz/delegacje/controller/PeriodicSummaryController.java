package pl.pracainz.delegacje.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import pl.pracainz.delegacje.model.Delegation;
import pl.pracainz.delegacje.service.DelegationService;

import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class PeriodicSummaryController {
    @Autowired
    DelegationService delegationService;

    @RequestMapping(value="/management/periodic-summary", method = RequestMethod.GET)
    public ModelAndView showPeriodicSummaryForCurrentYear(ModelAndView modelAndView){
        ArrayList<Delegation> delegationList = new ArrayList<Delegation>();
        delegationList.addAll(getFinishedDelegations(delegationService.getAll()));
        ArrayList<Delegation> filteredDelegationList = new ArrayList<Delegation>();
        filteredDelegationList.addAll(filterDelegationsByYear(getCurrentYear(), delegationList));
        if(!delegationList.isEmpty()) {
            modelAndView.setViewName("/management/periodic-summary");
            modelAndView.addObject("years", createListOfYears(delegationList));
            modelAndView.addObject("delegations", filteredDelegationList);
            modelAndView.addObject("summaryMessage", "Całkowity koszt roczny: " + getYearSummary(filteredDelegationList) + " [PLN]");
        } else {
            modelAndView.setViewName("/management/periodic-summary-empty");
        }
        return modelAndView;
    }

    @RequestMapping(value="/management/periodic-summary", method = RequestMethod.POST)
    public ModelAndView showPeriodicSummaryForYear(ModelAndView modelAndView, @RequestParam("delegationYear") String year){
        ArrayList<Delegation> delegationList = new ArrayList<Delegation>();
        delegationList.addAll(getFinishedDelegations(delegationService.getAll()));
        ArrayList<Delegation> filteredDelegationList = new ArrayList<Delegation>();
        filteredDelegationList.addAll(filterDelegationsByYear(Integer.parseInt(year), delegationList));
        modelAndView.setViewName("/management/periodic-summary");
        modelAndView.addObject("years", createListOfYears(delegationList));
        modelAndView.addObject("delegations",filteredDelegationList);
        modelAndView.addObject("summaryMessage", "Całkowity koszt roczny: " + getYearSummary(filteredDelegationList) + " [PLN]");
        return modelAndView;
    }

    private List<Delegation> filterDelegationsByYear(int year, ArrayList<Delegation> delegationList) {

        Collections
                .sort(delegationList, (d1, d2) -> {
            return d2.getId() - d1.getId();
        });

        delegationList.removeIf(delegation -> convertToYear(delegation.getRealEndDateTime()) != year);

        return delegationList;
    }

    private int convertToYear(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        String year = formatter.format(date);
        return Integer.parseInt(year);
    }

    private List<Integer> createListOfYears(ArrayList<Delegation> delegations) {
        ArrayList<Integer> yearsList = new ArrayList<>();
        for (Delegation delegation: delegations) {
            yearsList.add(convertToYear(delegation.getRealEndDateTime()));
        }

        List<Integer> yearsWithoutDuplicates = yearsList.stream().distinct().collect(Collectors.toList());
        return yearsWithoutDuplicates;
    }

    private List<Delegation> getFinishedDelegations(List<Delegation> delegationList) {
        delegationList.removeIf(delegation -> !delegation.getDelegationStatus().getStatus().equals("SETTLED"));
        return delegationList;
    }

    private int getCurrentYear() {
        return Year.now().getValue();
    }

    private double getYearSummary(ArrayList<Delegation> filteredDelegationList) {
        double sum = 0;
        for (Delegation delegation : filteredDelegationList) {
            sum += delegation.getSummaryCost() + delegation.getAdvancedPayment();
        }
        return sum;
    }
}
