package com.ols.lifelog.repository;

import com.ols.lifelog.domain.EventLogType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the EventLogType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventLogTypeRepository extends JpaRepository<EventLogType, Long> {}
