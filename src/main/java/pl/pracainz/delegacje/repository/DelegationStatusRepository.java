package pl.pracainz.delegacje.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.pracainz.delegacje.model.Delegation;
import pl.pracainz.delegacje.model.DelegationStatus;

@Repository
public interface DelegationStatusRepository extends JpaRepository<DelegationStatus, Long> {
    DelegationStatus findById(int id);
    DelegationStatus findByStatus(String status);
    void deleteById(int id);
}
