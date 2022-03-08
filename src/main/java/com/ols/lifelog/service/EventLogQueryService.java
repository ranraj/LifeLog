package com.ols.lifelog.service;

import com.ols.lifelog.domain.*; // for static metamodels
import com.ols.lifelog.domain.EventLog;
import com.ols.lifelog.repository.EventLogRepository;
import com.ols.lifelog.service.criteria.EventLogCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link EventLog} entities in the database.
 * The main input is a {@link EventLogCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EventLog} or a {@link Page} of {@link EventLog} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EventLogQueryService extends QueryService<EventLog> {

    private final Logger log = LoggerFactory.getLogger(EventLogQueryService.class);

    private final EventLogRepository eventLogRepository;

    public EventLogQueryService(EventLogRepository eventLogRepository) {
        this.eventLogRepository = eventLogRepository;
    }

    /**
     * Return a {@link List} of {@link EventLog} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EventLog> findByCriteria(EventLogCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EventLog> specification = createSpecification(criteria);
        return eventLogRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link EventLog} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EventLog> findByCriteria(EventLogCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EventLog> specification = createSpecification(criteria);
        return eventLogRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EventLogCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EventLog> specification = createSpecification(criteria);
        return eventLogRepository.count(specification);
    }

    /**
     * Function to convert {@link EventLogCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EventLog> createSpecification(EventLogCriteria criteria) {
        Specification<EventLog> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EventLog_.id));
            }
            if (criteria.getUuid() != null) {
                specification = specification.and(buildSpecification(criteria.getUuid(), EventLog_.uuid));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), EventLog_.name));
            }
            if (criteria.getDetail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDetail(), EventLog_.detail));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), EventLog_.createdDate));
            }
            if (criteria.getUpdatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedDate(), EventLog_.updatedDate));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(EventLog_.user, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getTagsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTagsId(), root -> root.join(EventLog_.tags, JoinType.LEFT).get(Tags_.id))
                    );
            }
            if (criteria.getEventLogBookId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEventLogBookId(),
                            root -> root.join(EventLog_.eventLogBook, JoinType.LEFT).get(EventLogBook_.id)
                        )
                    );
            }
            if (criteria.getEventLogTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEventLogTypeId(),
                            root -> root.join(EventLog_.eventLogTypes, JoinType.LEFT).get(EventLogType_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
