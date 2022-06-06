package pl.pracainz.delegacje.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.pracainz.delegacje.model.Delegation;
import pl.pracainz.delegacje.model.DelegationStatus;
import pl.pracainz.delegacje.model.User;
import pl.pracainz.delegacje.repository.DelegationRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class DelegationService {

    @Autowired
    DelegationRepository delegationRepository;
    @Autowired
    DelegationStatusService delegationStatusService;
    @Autowired
    UserService userService;

    public List<Delegation> getAll() {
        return (List<Delegation>) delegationRepository.findAll();
    }

    public Delegation getById(int id) { return delegationRepository.findById(id); }

    public void deleteById(int id) { delegationRepository.deleteById(id); }

    public Delegation saveDelegation(Delegation delegation) throws ParseException {
        delegation.setUser(userService.getLoggedInUser());

        java.util.Date date=new java.util.Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        delegation.setSubmissionDate(formatter.parse(formatter.format(date)));

        System.out.println(formatter.parse(delegation.getStartDateTemp()));

        delegation.setStartDate(formatter.parse(delegation.getStartDateTemp()));
        delegation.setEndDate(formatter.parse(delegation.getEndDateTemp()));

        System.out.println(delegation.getEndDate());
        System.out.println(delegation.getStartDate());

        delegation.setAccountantId(0);
        delegation.setRealStartDateTime(null);
        delegation.setRealEndDateTime(null);
        delegation.setAdvancedPayment(Double.parseDouble(delegation.getPaymentTemp()));

        delegation.setDelegationStatus(delegationStatusService.getById(1)); //np. 1 - oczekujace na akceptacje
        //delegation.setStatus(delegationStatusService.getByName("WAITING FOR ACCEPTANCE"));
        //---?


        return delegationRepository.save(delegation);
    }
    public Page<Delegation> listAll(int pageNumber, String sortField, String sortDir, String keyword, String role, int id) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNumber - 1, 6, sort);

        if(keyword != null) {
            if(role.equals("USER"))
                return delegationRepository.findAllEmployeeDelegations( id, keyword, pageable);

            return delegationRepository.findAll(keyword, pageable);
        }
        if(role.equals("USER"))
            return delegationRepository.findAllEmployeeDelegations( id,"", pageable);

        return delegationRepository.findAll(pageable);
    }

}
