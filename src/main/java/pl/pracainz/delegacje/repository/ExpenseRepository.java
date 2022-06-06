package pl.pracainz.delegacje.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.pracainz.delegacje.model.Delegation;
import pl.pracainz.delegacje.model.Expense;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    Expense findById(int id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "DELETE FROM expenses WHERE expense_id = ?1", nativeQuery = true)
    void deleteById(long id);

    List<Expense> findExpensesByDelegation_Id(int delegationId);

}
