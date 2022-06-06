package pl.pracainz.delegacje.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import pl.pracainz.delegacje.model.Delegation;
import pl.pracainz.delegacje.model.User;
import pl.pracainz.delegacje.service.DelegationService;
import pl.pracainz.delegacje.service.RoleService;
import pl.pracainz.delegacje.service.UserService;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

@Controller
public class AddDelegationController {

    @Autowired
    DelegationService delegationService;

    @RequestMapping(value="/user/create-delegation", method = RequestMethod.GET)
    public ModelAndView newRegistrationForm(ModelAndView modelAndView ){
        modelAndView.addObject("delegation", new Delegation());
        modelAndView.setViewName("/user/create-delegation");
        return modelAndView;
    }
    @RequestMapping(value="/user/create-delegation", method = RequestMethod.POST)
    public ModelAndView registerUserInDatabase(ModelAndView modelAndView, @Valid Delegation delegation,
                                               BindingResult bindingResult) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = formatter.parse(delegation.getStartDateTemp());
        Date endDate = formatter.parse(delegation.getEndDateTemp());

        if(startDate.after(endDate)) {
            bindingResult.addError(new FieldError("delegation", "startDate",
                    "*Błędnie wprowadzone daty\n"));
        }
        if(!bindingResult.hasErrors()) {
            delegationService.saveDelegation(delegation);
            modelAndView.addObject("delegation", new Delegation());
            modelAndView.addObject("successMessage", "Poprawnie utworzono delegację.");

        }
        modelAndView.setViewName("/user/create-delegation");
        return modelAndView;
    }

}
