package pl.pracainz.delegacje.controller;

import org.apache.tomcat.jni.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import pl.pracainz.delegacje.model.Password;
import pl.pracainz.delegacje.model.User;
import pl.pracainz.delegacje.repository.PasswordRepository;
import pl.pracainz.delegacje.repository.UserRepository;
import pl.pracainz.delegacje.service.UserService;

import javax.validation.Valid;

@Controller
public class ChangePasswordController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @RequestMapping(value="/auth/change-password", method = RequestMethod.GET)
    public ModelAndView getChangeUserPasswordForm(ModelAndView modelAndView) {
        User user = userService.getLoggedInUser();
        modelAndView.addObject("password", new Password());
        modelAndView.setViewName("/auth/change-password");
        return modelAndView;

    }
    @RequestMapping(value="/auth/change-password", method = RequestMethod.POST)
    public ModelAndView changeUserPassword(ModelAndView modelAndView, @Valid Password password, BindingResult bindingResult) {
        if(!(password.getPasswordHash().equals(password.getRetypePassword()))) {
            modelAndView.setViewName("/auth/change-password");
            bindingResult.addError(new FieldError("password", "passwordHash",
                    "*Hasła się nie zgadzają"));
            bindingResult.addError(new FieldError("password", "retypePassword", ""));
        }
        if(!bindingResult.hasErrors()) {
            User user = userService.getLoggedInUser();
            Password passwordToUpdate = user.getPassword();
            passwordToUpdate.setPasswordHash(bCryptPasswordEncoder.encode(password.getPasswordHash()));
            passwordToUpdate.setTemporary(false);
            user.setPassword(passwordToUpdate);
            userRepository.save(user);
            modelAndView.setViewName("redirect:/home");
        }
        return modelAndView;
    }

}