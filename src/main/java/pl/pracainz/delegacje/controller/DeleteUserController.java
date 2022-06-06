package pl.pracainz.delegacje.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import pl.pracainz.delegacje.model.User;
import pl.pracainz.delegacje.repository.UserRepository;
import pl.pracainz.delegacje.service.UserService;

@Controller
public class DeleteUserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;

    @RequestMapping(value = "/admin/delete-user/{id}", method = RequestMethod.GET)
    public RedirectView deleteUser(@PathVariable int id) {
        User user = userService.findUserById(id);
        user.setActive(!user.getActive());
        userRepository.save(user);
        return new RedirectView("/management/user-list");
    }
}
