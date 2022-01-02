package com.armaghanehayat.autism.service;

import com.armaghanehayat.autism.domain.*; // for static metamodels
import com.armaghanehayat.autism.domain.CeremonyUser;
import com.armaghanehayat.autism.repository.CeremonyUserRepository;
import com.armaghanehayat.autism.service.criteria.CeremonyUserCriteria;
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
 * Service for executing complex queries for {@link CeremonyUser} entities in the database.
 * The main input is a {@link CeremonyUserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CeremonyUser} or a {@link Page} of {@link CeremonyUser} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CeremonyUserQueryService extends QueryService<CeremonyUser> {

    private final Logger log = LoggerFactory.getLogger(CeremonyUserQueryService.class);

    private final CeremonyUserRepository ceremonyUserRepository;

    public CeremonyUserQueryService(CeremonyUserRepository ceremonyUserRepository) {
        this.ceremonyUserRepository = ceremonyUserRepository;
    }

    /**
     * Return a {@link List} of {@link CeremonyUser} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CeremonyUser> findByCriteria(CeremonyUserCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CeremonyUser> specification = createSpecification(criteria);
        return ceremonyUserRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link CeremonyUser} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CeremonyUser> findByCriteria(CeremonyUserCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CeremonyUser> specification = createSpecification(criteria);
        return ceremonyUserRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CeremonyUserCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CeremonyUser> specification = createSpecification(criteria);
        return ceremonyUserRepository.count(specification);
    }

    /**
     * Function to convert {@link CeremonyUserCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CeremonyUser> createSpecification(CeremonyUserCriteria criteria) {
        Specification<CeremonyUser> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CeremonyUser_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), CeremonyUser_.name));
            }
            if (criteria.getFamily() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFamily(), CeremonyUser_.family));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), CeremonyUser_.phoneNumber));
            }
            if (criteria.getHomeNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getHomeNumber(), CeremonyUser_.homeNumber));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), CeremonyUser_.address));
            }
            if (criteria.getCeremonyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCeremonyId(),
                            root -> root.join(CeremonyUser_.ceremonies, JoinType.LEFT).get(Ceremony_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
