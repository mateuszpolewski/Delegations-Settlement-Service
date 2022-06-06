package pl.pracainz.delegacje.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;
import pl.pracainz.delegacje.model.Delegation;
import pl.pracainz.delegacje.model.User;
import pl.pracainz.delegacje.repository.DelegationRepository;
import pl.pracainz.delegacje.repository.UserRepository;
import pl.pracainz.delegacje.service.DelegationService;
import pl.pracainz.delegacje.service.DelegationStatusService;
import pl.pracainz.delegacje.service.UserService;

@Controller
public class DeleteDelegationController {

    @Autowired
    DelegationRepository delegationRepository;
    @Autowired
    DelegationService delegationService;
    @Autowired
    DelegationStatusService delegationStatusService;

    @RequestMapping(value = "/user/delete-delegation/{id}", method = RequestMethod.GET)
    public RedirectView deleteUser(@PathVariable int id) {
        Delegation delegation = delegationService.getById(id);
        delegation.setDelegationStatus(delegationStatusService.getByStatus("CANCELLED"));
        delegationRepository.save(delegation);
        return new RedirectView("/auth/delegation-list");
    }
}
