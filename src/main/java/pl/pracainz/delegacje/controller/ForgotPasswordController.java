package pl.pracainz.delegacje.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import pl.pracainz.delegacje.model.Password;
import pl.pracainz.delegacje.model.User;
import pl.pracainz.delegacje.repository.UserRepository;
import pl.pracainz.delegacje.service.EmailService;
import pl.pracainz.delegacje.service.PasswordGenerator;
import pl.pracainz.delegacje.service.UserService;

import javax.mail.MessagingException;
import javax.validation.Valid;

@Controller
public class ForgotPasswordController {
    @Autowired
    UserService userService;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EmailService emailService;
    @RequestMapping(value="/forgot-password", method = RequestMethod.GET)
    public ModelAndView getForgotPasswordForm(ModelAndView modelAndView) {
        modelAndView.setViewName("/public/forgot-password");
        modelAndView.addObject("user",new User());
        PasswordGenerator password = new PasswordGenerator();
        System.out.println(bCryptPasswordEncoder.encode(password.generateRandomPassword()));
        return modelAndView;
    }
    @RequestMapping(value="/forgot-password", method = RequestMethod.POST)
    public ModelAndView resetPasswordAndSendEmail(ModelAndView modelAndView, @Valid User user, BindingResult bindingResult) throws MessagingException {
        User userToUpdate = userService.findUserByEmail(user.getEmail());
        modelAndView.setViewName("/public/forgot-password");
        if(userToUpdate == null)
            bindingResult.addError(new FieldError("user", "email", "*Nie znaleziono użytkownika o podanym adresie email"));
        if(!bindingResult.hasErrors()) {
            Password passwordToUpdate = userToUpdate.getPassword();
            String plainTextPassword = (new PasswordGenerator()).generateRandomPassword();
            passwordToUpdate.setPasswordHash(bCryptPasswordEncoder.encode(plainTextPassword));
            passwordToUpdate.setTemporary(true);
            userToUpdate.setPassword(passwordToUpdate);
            userRepository.save(userToUpdate);
            modelAndView.setViewName("redirect:/login");
            emailService.sendEmail(userToUpdate.getEmail(), "Reset hasła", "/email/forgot-email", plainTextPassword);
        }
        return modelAndView;
    }
}
