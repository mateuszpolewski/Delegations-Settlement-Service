package pl.pracainz.delegacje.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pracainz.delegacje.model.Role;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findById(int id);
    void deleteById(int id);
    Role findByRole(String role);
    List<Role> findAll();
}
