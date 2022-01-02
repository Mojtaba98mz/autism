package com.armaghanehayat.autism.service;

import com.armaghanehayat.autism.domain.*; // for static metamodels
import com.armaghanehayat.autism.domain.Ceremony;
import com.armaghanehayat.autism.repository.CeremonyRepository;
import com.armaghanehayat.autism.service.criteria.CeremonyCriteria;
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
 * Service for executing complex queries for {@link Ceremony} entities in the database.
 * The main input is a {@link CeremonyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Ceremony} or a {@link Page} of {@link Ceremony} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CeremonyQueryService extends QueryService<Ceremony> {

    private final Logger log = LoggerFactory.getLogger(CeremonyQueryService.class);

    private final CeremonyRepository ceremonyRepository;

    public CeremonyQueryService(CeremonyRepository ceremonyRepository) {
        this.ceremonyRepository = ceremonyRepository;
    }

    /**
     * Return a {@link List} of {@link Ceremony} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Ceremony> findByCriteria(CeremonyCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Ceremony> specification = createSpecification(criteria);
        return ceremonyRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Ceremony} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Ceremony> findByCriteria(CeremonyCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Ceremony> specification = createSpecification(criteria);
        return ceremonyRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CeremonyCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Ceremony> specification = createSpecification(criteria);
        return ceremonyRepository.count(specification);
    }

    /**
     * Function to convert {@link CeremonyCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Ceremony> createSpecification(CeremonyCriteria criteria) {
        Specification<Ceremony> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Ceremony_.id));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), Ceremony_.amount));
            }
            if (criteria.getGivenDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGivenDate(), Ceremony_.givenDate));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Ceremony_.description));
            }
            if (criteria.getCeremonyUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCeremonyUserId(),
                            root -> root.join(Ceremony_.ceremonyUser, JoinType.LEFT).get(CeremonyUser_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
