package pl.pracainz.delegacje.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import pl.pracainz.delegacje.model.Role;
import pl.pracainz.delegacje.model.User;
import pl.pracainz.delegacje.repository.RoleRepository;
import pl.pracainz.delegacje.repository.UserRepository;
import pl.pracainz.delegacje.service.RoleService;
import pl.pracainz.delegacje.service.UserService;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashSet;

@Controller
public class EditProfileController {
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

    @RequestMapping(value = "/edit-profile", method = RequestMethod.GET)
    public ModelAndView getEditUserForm(ModelAndView modelAndView) {
        User user = userService.getLoggedInUser();
        user.setChosenRole(user.getRoles().stream().findFirst().get().getRole());
        modelAndView.addObject("user", user);
        modelAndView.addObject("roles", roleService.getAll());
        modelAndView.setViewName("auth/edit-profile");
        return modelAndView;
    }
    @RequestMapping(value="/edit-profile", method = RequestMethod.POST)
    public ModelAndView editUser(ModelAndView modelAndView, @PathVariable int id, @Valid User user,
                                 BindingResult bindingResult) throws MessagingException {
        User userToUpdate = userService.findUserById(user.getId());

        if(!bindingResult.hasErrors()) {
            userToUpdate = getUpdatedUser(userToUpdate, user);
            userRepository.save(userToUpdate);
            modelAndView.addObject("successMessage", "Poprawnie edytowano użytkownika.");

        }
        modelAndView.addObject("user", userToUpdate);
        modelAndView.addObject("roles", roleService.getAll());
        modelAndView.setViewName("auth/edit-profile");
        return modelAndView;
    }
    private User getUpdatedUser(User userToUpdate, User user) {
        userToUpdate.setName(user.getName());
        userToUpdate.setLastName(user.getLastName());
        userToUpdate.setPosition(user.getPosition());
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setChosenRole(user.getChosenRole());
        userToUpdate.setComment(user.getComment());
        userToUpdate.setPhoneNumber(user.getPhoneNumber());
        Role userRole = roleRepository.findByRole(user.getChosenRole());
        userToUpdate.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        return userToUpdate;
    }
}
