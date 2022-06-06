package pl.pracainz.delegacje.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import pl.pracainz.delegacje.model.Delegation;
import pl.pracainz.delegacje.model.DelegationStatus;
import pl.pracainz.delegacje.repository.DelegationRepository;
import pl.pracainz.delegacje.service.DelegationService;
import pl.pracainz.delegacje.service.DelegationStatusService;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class FinishDelegationController {
    @Autowired
    DelegationService delegationService;
    @Autowired
    DelegationStatusService delegationStatusService;
    @Autowired
    DelegationRepository delegationRepository;

    @RequestMapping(value="/user/delegation/{id}/finish-delegation", method = RequestMethod.GET)
    public ModelAndView newRegistrationForm(ModelAndView modelAndView, @PathVariable int id ){
        modelAndView.addObject("delegation", delegationService.getById(id));
        modelAndView.addObject("delegationId", id);
        modelAndView.setViewName("/user/finish-delegation");
        return modelAndView;
    }
    @RequestMapping(value="/user/delegation/{id}/finish-delegation", method = RequestMethod.POST)
    public ModelAndView registerUserInDatabase(ModelAndView modelAndView, @PathVariable int id,@Valid Delegation delegation,
                                               BindingResult bindingResult) throws ParseException {
        modelAndView.setViewName("/user/finish-delegation");
        Delegation delegationToSave = delegationService.getById(id);
        delegationToSave.setRealStartDateTime(constructRealDateTime(delegation.getStartDateTemp(),delegation.getStartHourTemp()));
        delegationToSave.setRealEndDateTime(constructRealDateTime(delegation.getEndDateTemp(),delegation.getEndHourTemp()));
        delegationToSave.setDelegationStatus(delegationStatusService.getByStatus("FINISHED"));
        delegationRepository.save(delegationToSave);
        modelAndView.setViewName("redirect:/auth/delegation/{id}");
        return modelAndView;
    }
    private Date constructRealDateTime(String date, String hour) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return simpleDateFormat.parse(date + " " +  hour);
    }
}
