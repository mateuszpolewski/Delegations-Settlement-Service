package pl.pracainz.delegacje.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pracainz.delegacje.model.DelegationStatus;
import pl.pracainz.delegacje.repository.DelegationStatusRepository;

@Service
public class DelegationStatusService {
    @Autowired
    DelegationStatusRepository delegationStatusRepository;

    public DelegationStatus getById(int id) {return (DelegationStatus) delegationStatusRepository.findById(id); }
    public DelegationStatus getByStatus(String name) {return (DelegationStatus) delegationStatusRepository.findByStatus(name); }
}
