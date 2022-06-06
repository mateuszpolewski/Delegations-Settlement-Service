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
import pl.pracainz.delegacje.model.Role;
import pl.pracainz.delegacje.model.User;
import pl.pracainz.delegacje.repository.DelegationRepository;
import pl.pracainz.delegacje.repository.RoleRepository;
import pl.pracainz.delegacje.repository.UserRepository;
import pl.pracainz.delegacje.service.DelegationService;
import pl.pracainz.delegacje.service.RoleService;
import pl.pracainz.delegacje.service.UserService;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

@Controller
public class EditDelegationController {
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    DelegationRepository delegationRepository;
    @Autowired
    DelegationService delegationService;

    @RequestMapping(value = "/user/edit-delegation/{id}", method = RequestMethod.GET)
    public ModelAndView getEditDelegationForm(ModelAndView modelAndView, @PathVariable int id) {
        Delegation delegationToUpdate = delegationService.getById(id);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        delegationToUpdate.setStartDateTemp(formatter.format(delegationToUpdate.getStartDate()));
        delegationToUpdate.setEndDateTemp(formatter.format(delegationToUpdate.getEndDate()));
        delegationToUpdate.setPaymentTemp(Double.toString(delegationToUpdate.getAdvancedPayment()));
        modelAndView.addObject("delegation", delegationToUpdate);
        modelAndView.setViewName("/user/edit-delegation");
        return modelAndView;
    }
    @RequestMapping(value="/user/edit-delegation/{id}", method = RequestMethod.POST)
    public ModelAndView editDelegation(ModelAndView modelAndView, @PathVariable int id, @Valid Delegation delegation,
                                 BindingResult bindingResult) throws MessagingException, ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = formatter.parse(delegation.getStartDateTemp());
        Date endDate = formatter.parse(delegation.getEndDateTemp());

        Delegation delegationToUpdate = delegationService.getById(id);

        if(startDate.after(endDate)) {
            bindingResult.addError(new FieldError("delegation", "startDate",
                    "*Błędnie wprowadzone daty"));
        }
        if(!bindingResult.hasErrors()) {
            delegationToUpdate = getUpdatedDelegation(delegationToUpdate, delegation);
            delegationRepository.save(delegationToUpdate);
            modelAndView.addObject("successMessage", "Poprawnie edytowano delegację.");
            delegation = delegationToUpdate;
        }

        modelAndView.addObject("delegation", delegation);
        modelAndView.setViewName("/user/edit-delegation");
        return modelAndView;
    }

    private Delegation getUpdatedDelegation(Delegation delegationToUpdate, Delegation delegation) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = formatter.parse(delegation.getStartDateTemp());
        Date endDate = formatter.parse(delegation.getEndDateTemp());
        delegationToUpdate.setTitle(delegation.getTitle());
        delegationToUpdate.setAdvancedPayment(Double.parseDouble(delegation.getPaymentTemp()));
        delegationToUpdate.setDescription(delegation.getDescription());
        delegationToUpdate.setDestinationCity(delegation.getDestinationCity());
        delegationToUpdate.setDestinationCountry(delegation.getDestinationCountry());
        delegationToUpdate.setStartDate(startDate);
        delegationToUpdate.setEndDate(endDate);

        delegationToUpdate.setStartDateTemp(formatter.format(delegationToUpdate.getStartDate()));
        delegationToUpdate.setEndDateTemp(formatter.format(delegationToUpdate.getEndDate()));
        delegationToUpdate.setPaymentTemp(Double.toString(delegationToUpdate.getAdvancedPayment()));
        return delegationToUpdate;
    }
}

