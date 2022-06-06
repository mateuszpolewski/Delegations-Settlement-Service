package pl.pracainz.delegacje.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.pracainz.delegacje.model.Delegation;
import pl.pracainz.delegacje.model.User;

import java.util.List;

@Repository
public interface DelegationRepository extends JpaRepository<Delegation, Long> {
    Delegation findById(int id);
    void deleteById(int id);
    @Query("SELECT d FROM Delegation d JOIN d.user u WHERE CONCAT(d.id,' ',d.title, ' ', d.startDate, ' ', d.endDate, ' ', d.destinationCountry," +
            " ' ', d.destinationCity, ' ',u.name,' ',u.lastName) LIKE %?1%")
    Page<Delegation> findAll(String keyword, Pageable pageable);


    @Query("SELECT d FROM Delegation d JOIN d.user u WHERE CONCAT(d.id,' ',d.title, ' ', d.startDate, ' ', d.endDate, ' ', d.destinationCountry," +
            " ' ', d.destinationCity, ' ',u.name,' ',u.lastName) LIKE %:keyword% AND u.id=:userId")
    Page<Delegation> findAllEmployeeDelegations( @Param("userId") int userId,@Param("keyword") String keyword, Pageable pageable);

    List<Delegation> findDelegationsByUser_Id(int userId);
}

