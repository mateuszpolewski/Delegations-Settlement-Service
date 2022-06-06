package pl.pracainz.delegacje.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import pl.pracainz.delegacje.model.Delegation;
import pl.pracainz.delegacje.repository.DelegationRepository;
import pl.pracainz.delegacje.service.DelegationService;

import javax.validation.Valid;

@Controller
public class ShowDelegationController {
    @Autowired
    DelegationService delegationService;
    @Autowired
    DelegationRepository delegationRepository;

    @RequestMapping(value="/auth/delegation/{id}", method = RequestMethod.GET)
    public ModelAndView showDelegationPreview(ModelAndView modelAndView, @PathVariable int id){
        Delegation delegation = delegationService.getById(id);
        modelAndView.addObject("delegation", delegation);
        modelAndView.addObject("delegationForPayment", new Delegation());
        modelAndView.setViewName("/auth/delegation-preview");
        return modelAndView;
    }
    @RequestMapping(value="/auth/delegation/{id}", method = RequestMethod.POST)
    public ModelAndView delegationPreview(ModelAndView modelAndView, @PathVariable int id, @Valid Delegation delegation,
                                          BindingResult bindingResult){
        Delegation delegationToSave = delegationService.getById(id);
        delegationToSave.setPaymentTemp(delegation.getPaymentTemp());
        if(!bindingResult.hasErrors()) {
            delegationToSave.setAdvancedPayment(Double.parseDouble(delegationToSave.getPaymentTemp()));
            delegationRepository.save(delegationToSave);
            modelAndView.addObject("delegation", new Delegation());
            modelAndView.addObject("successMessage", "Zmieniono wnioskowaną zaliczkę");

        }
        modelAndView.setViewName("/auth/delegation-preview");
        modelAndView.addObject("delegation", delegationToSave);
        modelAndView.addObject("delegationForPayment", new Delegation());
        return modelAndView;

    }
}
