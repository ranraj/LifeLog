package com.ols.lifelog.repository;

import com.ols.lifelog.domain.EventLog;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the EventLog entity.
 */
@Repository
public interface EventLogRepository extends JpaRepository<EventLog, Long>, JpaSpecificationExecutor<EventLog> {
    @Query("select eventLog from EventLog eventLog where eventLog.user.login = ?#{principal.username}")
    List<EventLog> findByUserIsCurrentUser();

    @Query(
        value = "select distinct eventLog from EventLog eventLog left join fetch eventLog.tags",
        countQuery = "select count(distinct eventLog) from EventLog eventLog"
    )
    Page<EventLog> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct eventLog from EventLog eventLog left join fetch eventLog.tags")
    List<EventLog> findAllWithEagerRelationships();

    @Query("select eventLog from EventLog eventLog left join fetch eventLog.tags where eventLog.id =:id")
    Optional<EventLog> findOneWithEagerRelationships(@Param("id") Long id);
}
