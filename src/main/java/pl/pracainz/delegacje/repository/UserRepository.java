package pl.pracainz.delegacje.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.pracainz.delegacje.model.Role;
import pl.pracainz.delegacje.model.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findById(int id);
    void deleteById(int id);
    User findByEmail(String email);

    //@Query("SELECT u FROM User u WHERE CONCAT(u.id,' ',u.name, ' ', u.lastName, ' ', u.email, ' ', u.phoneNumber," +
    //        " ' ', u.position,' ', u.comment) LIKE %?1%")
    @Query("SELECT u FROM User u JOIN u.roles r WHERE CONCAT(u.id,' ',u.name, ' ', u.lastName, ' ', u.email, ' ', u.phoneNumber," +
            " ' ', u.position,' ', u.comment, ' ', r.role) LIKE %?1%")
    Page<User> findAll(String keyword, Pageable pageable);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE CONCAT(u.id,' ',u.name, ' ', u.lastName, ' ', u.email, ' ', u.phoneNumber," +
            " ' ', u.position,' ', u.comment) LIKE %?1% AND r.role = 'USER'")
    Page<User> findAllEmployees(String keyword, Pageable pageable);
}
