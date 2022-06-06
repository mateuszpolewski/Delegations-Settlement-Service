package pl.pracainz.delegacje.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import pl.pracainz.delegacje.model.Delegation;
import pl.pracainz.delegacje.model.User;
import pl.pracainz.delegacje.service.DelegationService;
import pl.pracainz.delegacje.service.UserService;

import java.util.List;

@Controller
public class ShowDelegationListController {
    @Autowired
    DelegationService delegationService;
    @Autowired
    UserService userService;

    @RequestMapping(value="/auth/delegation-list", method = RequestMethod.GET)
    public ModelAndView showFirstPage(ModelAndView modelAndView){
        String keyword = " ";
        return showUsers(modelAndView, 1, "id", "asc", keyword);
    }

    @RequestMapping(value="/auth/delegation-list/page/{pageNumber}", method = RequestMethod.GET)
    public ModelAndView showUsers(ModelAndView modelAndView, @PathVariable("pageNumber") int currentPage,
                                  @Param("sortField") String sortField, @Param("sortField") String sortDir,
                                  @Param("keyword") String keyword){


        User user = userService.getLoggedInUser();
        String userRole = user.getRoles().stream().findFirst().get().getRole();
        int userId = user.getId();
        Page<Delegation> page = delegationService.listAll(currentPage, sortField, sortDir, keyword, userRole, userId);

        List<Delegation> delegationList = page.getContent();
        /*
        User user = userService.getLoggedInUser();
        String userRole = user.getRoles().stream().findFirst().get().getRole();

        if(userRole == "USER") {
            for (Delegation delegation:
                 delegationList) {
                delegationList.removeIf(del -> user.getId() != del.getUser().getId());
            }
        }
        */
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("totalPages", page.getTotalPages());
        modelAndView.addObject("totalItems", page.getTotalElements());
        modelAndView.addObject("delegations", delegationList);
        modelAndView.addObject("sortField", sortField);
        modelAndView.addObject("sortDir", sortDir);
        modelAndView.addObject("keyword", keyword);

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
        modelAndView.addObject("reverseSortDir", reverseSortDir);

        modelAndView.setViewName("/user/list-of-delegations");
        return modelAndView;
    }

}
