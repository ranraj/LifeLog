package com.ols.lifelog.service;

import com.ols.lifelog.domain.*; // for static metamodels
import com.ols.lifelog.domain.EventLogBook;
import com.ols.lifelog.repository.EventLogBookRepository;
import com.ols.lifelog.service.criteria.EventLogBookCriteria;
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
 * Service for executing complex queries for {@link EventLogBook} entities in the database.
 * The main input is a {@link EventLogBookCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EventLogBook} or a {@link Page} of {@link EventLogBook} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EventLogBookQueryService extends QueryService<EventLogBook> {

    private final Logger log = LoggerFactory.getLogger(EventLogBookQueryService.class);

    private final EventLogBookRepository eventLogBookRepository;

    public EventLogBookQueryService(EventLogBookRepository eventLogBookRepository) {
        this.eventLogBookRepository = eventLogBookRepository;
    }

    /**
     * Return a {@link List} of {@link EventLogBook} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EventLogBook> findByCriteria(EventLogBookCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EventLogBook> specification = createSpecification(criteria);
        return eventLogBookRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link EventLogBook} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EventLogBook> findByCriteria(EventLogBookCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EventLogBook> specification = createSpecification(criteria);
        return eventLogBookRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EventLogBookCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EventLogBook> specification = createSpecification(criteria);
        return eventLogBookRepository.count(specification);
    }

    /**
     * Function to convert {@link EventLogBookCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EventLogBook> createSpecification(EventLogBookCriteria criteria) {
        Specification<EventLogBook> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EventLogBook_.id));
            }
            if (criteria.getUuid() != null) {
                specification = specification.and(buildSpecification(criteria.getUuid(), EventLogBook_.uuid));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), EventLogBook_.createdDate));
            }
            if (criteria.getUpdatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedDate(), EventLogBook_.updatedDate));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), EventLogBook_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), EventLogBook_.description));
            }
            if (criteria.getArchieved() != null) {
                specification = specification.and(buildSpecification(criteria.getArchieved(), EventLogBook_.archieved));
            }
            if (criteria.getEventLogId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEventLogId(),
                            root -> root.join(EventLogBook_.eventLogs, JoinType.LEFT).get(EventLog_.id)
                        )
                    );
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(EventLogBook_.user, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
