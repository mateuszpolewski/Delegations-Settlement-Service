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
public class EditUserController {
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

    @RequestMapping(value = "/admin/edit-user/{id}", method = RequestMethod.GET)
    public ModelAndView getEditUserForm(ModelAndView modelAndView, @PathVariable int id) {
        User user = userService.findUserById(id);
        user.setChosenRole(userService.getRoleNameByUserId(id));

        modelAndView.addObject("user", user);
        modelAndView.addObject("roles", roleService.getAll());
        modelAndView.setViewName("admin/edit-user");
        return modelAndView;
    }
    @RequestMapping(value="/admin/edit-user/{id}", method = RequestMethod.POST)
    public ModelAndView editUser(ModelAndView modelAndView, @PathVariable int id, @Valid User user,
                                               BindingResult bindingResult) throws MessagingException {
        User userToUpdate = userService.findUserById(user.getId());
        /*
        if(userService.findUserByEmail(user.getEmail()) != null) {
            bindingResult.addError(new FieldError("user", "email",
                    "Istnieje użytkownik z takim adresem e-mail"));
        }
        */
        if(!bindingResult.hasErrors()) {
            userToUpdate = getUpdatedUser(userToUpdate, user);
            userRepository.save(userToUpdate);
            modelAndView.addObject("successMessage", "Poprawnie edytowano użytkownika.");

        }
        modelAndView.addObject("user", userToUpdate);
        modelAndView.addObject("roles", roleService.getAll());
        modelAndView.setViewName("admin/edit-user");
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
