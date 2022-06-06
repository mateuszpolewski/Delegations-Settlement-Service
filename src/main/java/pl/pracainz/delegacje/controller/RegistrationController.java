package pl.pracainz.delegacje.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import pl.pracainz.delegacje.model.Role;
import pl.pracainz.delegacje.model.User;
import pl.pracainz.delegacje.service.RoleService;
import pl.pracainz.delegacje.service.UserService;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.List;

@Controller
public class RegistrationController {

    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;

    @RequestMapping(value="/admin/registration", method = RequestMethod.GET)
    public ModelAndView newRegistrationForm(ModelAndView modelAndView ){
        modelAndView.addObject("user", new User());
        modelAndView.addObject("roles", roleService.getAll());
        modelAndView.setViewName("/admin/registration");
        return modelAndView;
    }
    @RequestMapping(value="/admin/registration", method = RequestMethod.POST)
    public ModelAndView registerUserInDatabase(ModelAndView modelAndView, @Valid User user,
                                               BindingResult bindingResult) throws MessagingException {
        if(userService.findUserByEmail(user.getEmail()) != null) {
            bindingResult.addError(new FieldError("user", "email",
                    "Istnieje użytkownik z takim adresem e-mail"));
        }
        if(!bindingResult.hasErrors()) {
            userService.saveUser(user);
            modelAndView.addObject("user", new User());
            modelAndView.addObject("successMessage", "Poprawnie zarejstrowano użytkownika.");
        }
        modelAndView.addObject("roles", roleService.getAll());
        return modelAndView;
    }
}
