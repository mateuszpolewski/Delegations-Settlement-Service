package pl.pracainz.delegacje.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.pracainz.delegacje.model.Password;


@Repository
public interface PasswordRepository extends JpaRepository<Password, Long> {
    Password findById(int id);
    void deleteById(int id);
}
