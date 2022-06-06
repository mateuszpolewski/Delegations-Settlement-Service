package pl.pracainz.delegacje.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pracainz.delegacje.model.Category;
import pl.pracainz.delegacje.model.Role;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findById(int id);
    Category findByName(String name);
    List<Category> findAll();
    void deleteById(int id);
}
