package com.apptime.auth.repository;

import com.apptime.auth.model.AllUserTaskSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

/**
 * @author Qi Zhang
 * The DAO to access data of AllUserTaskSummary
 * Use Case: TMGP4-320
 */
public interface AllUserTaskSummaryRepository extends JpaRepository<AllUserTaskSummary, Integer> {
    @Query("SELECT s FROM AllUserTaskSummary s WHERE s.categoryId = ?1")
    AllUserTaskSummary findByCategory(int categoryId);

    @Query("SELECT s FROM AllUserTaskSummary s WHERE s.categoryId IN ( ?1) ")
    List<AllUserTaskSummary> findByCategoryIds(Collection<Integer> categoryIds);
}
