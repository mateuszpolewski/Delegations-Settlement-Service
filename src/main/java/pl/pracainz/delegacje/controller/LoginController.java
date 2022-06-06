package pl.pracainz.delegacje.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import pl.pracainz.delegacje.model.Delegation;
import pl.pracainz.delegacje.model.User;
import pl.pracainz.delegacje.repository.ExpenseRepository;
import pl.pracainz.delegacje.service.DelegationService;
import pl.pracainz.delegacje.service.ExpenseService;
import pl.pracainz.delegacje.service.UserService;

import javax.mail.MessagingException;

@Controller
public class LoginController {
    @Autowired
    UserService userService;
    @Autowired
    DelegationService delegationService;

    @RequestMapping(value="/")
    public RedirectView getWelcomePage(ModelAndView modelAndView) throws MessagingException{
        if(userService.findUserById(1) == null) createAdmin();
        return new RedirectView("/login");
    }

    @RequestMapping(value="/login", method = RequestMethod.GET)
    public ModelAndView getLoginForm1(ModelAndView modelAndView, @RequestParam(required = false) boolean error) {
        modelAndView.setViewName("/public/login");
        if(error)
            modelAndView.addObject("failMessage", "Błędnie wprowadzone dane");

        return modelAndView;

    }

    @RequestMapping(value="/home", method = RequestMethod.GET)
    public ModelAndView getHomePage(ModelAndView modelAndView) {
        modelAndView.addObject("numOfWaitingDelegations",numberOfWaitingDelegations());
        modelAndView.setViewName("/auth/home");
        return modelAndView;

    }
    @RequestMapping(value="/admin/page", method = RequestMethod.GET)
    public ModelAndView getAdminPage(ModelAndView modelAndView) {
        modelAndView.setViewName("/admin/admin-page");
        return modelAndView;

    }

    public void createAdmin() throws MessagingException{
        User user = new User();
        user.setName("Wojciech");
        user.setLastName("Nowak");
        user.setEmail("admin@admin.pl");
        user.setPhoneNumber("284723173");
        user.setPosition("Admin");
        userService.saveAdmin(user);
    }
    public int numberOfWaitingDelegations() {
        int i = 0;
        for ( Delegation delegation: delegationService.getAll()) {
            if(delegation.getDelegationStatus().getStatus().equals("WAITING")) {
                i++;
            }
        }
        return i;
    }
}
