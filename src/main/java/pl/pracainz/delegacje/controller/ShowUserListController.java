package pl.pracainz.delegacje.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import pl.pracainz.delegacje.model.User;
import pl.pracainz.delegacje.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@Controller
public class ShowUserListController {
    @Autowired
    UserService userService;

    @RequestMapping(value="/management/user-list", method = RequestMethod.GET)
    public ModelAndView showFirstPage(ModelAndView modelAndView){
        String keyword = " ";
        return showUsers(modelAndView, 1, "id", "asc", keyword);
    }

    @RequestMapping(value="/management/user-list/page/{pageNumber}", method = RequestMethod.GET)
    public ModelAndView showUsers(ModelAndView modelAndView, @PathVariable("pageNumber") int currentPage,
                                            @Param("sortField") String sortField, @Param("sortField") String sortDir,
                                            @Param("keyword") String keyword){


        User user = userService.getLoggedInUser();
        String userRole = user.getRoles().stream().findFirst().get().getRole();
        Page<User> page = userService.listAll(currentPage, sortField, sortDir, keyword, userRole);

        List<User> userList = page.getContent();

        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("totalPages", page.getTotalPages());
        //modelAndView.addObject("totalItems", page.getTotalElements());
        modelAndView.addObject("users", userList);
        modelAndView.addObject("sortField", sortField);
        modelAndView.addObject("sortDir", sortDir);
        modelAndView.addObject("keyword", keyword);

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
        modelAndView.addObject("reverseSortDir", reverseSortDir);

        modelAndView.setViewName("/management/list-of-users");
        return modelAndView;
    }

    private List<Long> indexToList(int start, int end) {
        return LongStream.rangeClosed(start, end)
                .boxed()
                .collect(Collectors.toList());
    }
}

