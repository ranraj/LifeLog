package com.ols.lifelog.repository;

import com.ols.lifelog.domain.EventLogBook;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the EventLogBook entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventLogBookRepository extends JpaRepository<EventLogBook, Long>, JpaSpecificationExecutor<EventLogBook> {
    @Query("select eventLogBook from EventLogBook eventLogBook where eventLogBook.user.login = ?#{principal.username}")
    List<EventLogBook> findByUserIsCurrentUser();
}
