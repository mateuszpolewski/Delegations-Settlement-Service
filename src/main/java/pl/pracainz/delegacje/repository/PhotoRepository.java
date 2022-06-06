package pl.pracainz.delegacje.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.pracainz.delegacje.model.Photo;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    Photo findById(int id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "DELETE FROM photos WHERE photo_id = ?1", nativeQuery = true)
    void deleteById(long id);
}
