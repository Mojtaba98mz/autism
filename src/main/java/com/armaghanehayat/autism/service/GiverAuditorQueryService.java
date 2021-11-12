package com.armaghanehayat.autism.service;

import com.armaghanehayat.autism.domain.*; // for static metamodels
import com.armaghanehayat.autism.domain.GiverAuditor;
import com.armaghanehayat.autism.repository.GiverAuditorRepository;
import com.armaghanehayat.autism.service.criteria.GiverAuditorCriteria;
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
 * Service for executing complex queries for {@link GiverAuditor} entities in the database.
 * The main input is a {@link GiverAuditorCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GiverAuditor} or a {@link Page} of {@link GiverAuditor} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GiverAuditorQueryService extends QueryService<GiverAuditor> {

    private final Logger log = LoggerFactory.getLogger(GiverAuditorQueryService.class);

    private final GiverAuditorRepository giverAuditorRepository;

    public GiverAuditorQueryService(GiverAuditorRepository giverAuditorRepository) {
        this.giverAuditorRepository = giverAuditorRepository;
    }

    /**
     * Return a {@link List} of {@link GiverAuditor} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GiverAuditor> findByCriteria(GiverAuditorCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<GiverAuditor> specification = createSpecification(criteria);
        return giverAuditorRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link GiverAuditor} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GiverAuditor> findByCriteria(GiverAuditorCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GiverAuditor> specification = createSpecification(criteria);
        return giverAuditorRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GiverAuditorCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<GiverAuditor> specification = createSpecification(criteria);
        return giverAuditorRepository.count(specification);
    }

    /**
     * Function to convert {@link GiverAuditorCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<GiverAuditor> createSpecification(GiverAuditorCriteria criteria) {
        Specification<GiverAuditor> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), GiverAuditor_.id));
            }
            if (criteria.getFieldName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFieldName(), GiverAuditor_.fieldName));
            }
            if (criteria.getOldValue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOldValue(), GiverAuditor_.oldValue));
            }
            if (criteria.getNewValue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNewValue(), GiverAuditor_.newValue));
            }
            if (criteria.getChangeDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getChangeDate(), GiverAuditor_.changeDate));
            }
            if (criteria.getAuditorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getAuditorId(), root -> root.join(GiverAuditor_.auditor, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getGiverId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getGiverId(), root -> root.join(GiverAuditor_.giver, JoinType.LEFT).get(Giver_.id))
                    );
            }
        }
        return specification;
    }
}
