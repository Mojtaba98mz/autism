package com.armaghanehayat.autism.service;

import com.armaghanehayat.autism.domain.*; // for static metamodels
import com.armaghanehayat.autism.domain.Giver;
import com.armaghanehayat.autism.repository.GiverRepository;
import com.armaghanehayat.autism.security.SecurityUtils;
import com.armaghanehayat.autism.service.criteria.GiverCriteria;
import java.util.List;
import java.util.Optional;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.LongFilter;

/**
 * Service for executing complex queries for {@link Giver} entities in the database.
 * The main input is a {@link GiverCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Giver} or a {@link Page} of {@link Giver} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GiverQueryService extends QueryService<Giver> {

    private final Logger log = LoggerFactory.getLogger(GiverQueryService.class);

    private final GiverRepository giverRepository;

    private final UserService userService;

    public GiverQueryService(GiverRepository giverRepository, UserService userService) {
        this.giverRepository = giverRepository;
        this.userService = userService;
    }

    /**
     * Return a {@link List} of {@link Giver} which matches the criteria from the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Giver> findByCriteria(GiverCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Giver> specification = createSpecification(criteria);
        return giverRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Giver} which matches the criteria from the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Giver> findByCriteria(GiverCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Giver> specification = createSpecification(criteria);
        return giverRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GiverCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Giver> specification = createSpecification(criteria);
        return giverRepository.count(specification);
    }

    /**
     * Function to convert {@link GiverCriteria} to a {@link Specification}
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Giver> createSpecification(GiverCriteria criteria) {
        Specification<Giver> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Giver_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Giver_.name));
            }
            if (criteria.getFamily() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFamily(), Giver_.family));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), Giver_.phoneNumber));
            }
            if (criteria.getHomeNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getHomeNumber(), Giver_.homeNumber));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), Giver_.address));
            }
            if (criteria.getAbsorbDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAbsorbDate(), Giver_.absorbDate));
            }
            if (criteria.getProvinceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getProvinceId(), root -> root.join(Giver_.province, JoinType.LEFT).get(Province_.id))
                    );
            }
            if (criteria.getProvinceName() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProvinceName(),
                            root -> root.join(Giver_.province, JoinType.LEFT).get(Province_.NAME)
                        )
                    );
            }
            /* if (criteria.getDisabled() != null) {
                specification = specification.and(buildSpecification(criteria.getDisabled(), Giver_.disabled));
            }*/
            if (criteria.getCityId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCityId(), root -> root.join(Giver_.city, JoinType.LEFT).get(City_.id))
                    );
            }
            if (criteria.getCityName() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCityName(), root -> root.join(Giver_.city, JoinType.LEFT).get(City_.name))
                    );
            }
            if (criteria.getDonationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getDonationId(), root -> root.join(Giver_.donations, JoinType.LEFT).get(Donation_.id))
                    );
            }
            if (criteria.getGiverauditorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getGiverauditorId(),
                            root -> root.join(Giver_.giverauditors, JoinType.LEFT).get(GiverAuditor_.id)
                        )
                    );
            }
            if (criteria.getAbsorbantId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getAbsorbantId(), root -> root.join(Giver_.absorbant, JoinType.LEFT).get(User_.id))
                    );
            }
            Optional<User> userWithAuthorities = userService.getUserWithAuthorities();
            if (userWithAuthorities.isPresent()) if (!userWithAuthorities.get().hasRole("ROLE_ADMIN")) {
                LongFilter longFilter = new LongFilter();
                longFilter.setEquals(userWithAuthorities.get().getId());
                specification =
                    specification.and(buildSpecification(longFilter, root -> root.join(Giver_.supporter, JoinType.LEFT).get(User_.id)));
                BooleanFilter booleanFilter = new BooleanFilter();
                booleanFilter.setEquals(false);
                specification = specification.and(buildSpecification(booleanFilter, Giver_.disabled));
            }
            if (criteria.getSupporterId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getSupporterId(), root -> root.join(Giver_.supporter, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getSupporterName() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSupporterName(),
                            root -> root.join(Giver_.supporter, JoinType.LEFT).get(User_.firstName)
                        )
                    );
            }
            if (criteria.getSupporterFamily() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSupporterFamily(),
                            root -> root.join(Giver_.supporter, JoinType.LEFT).get(User_.lastName)
                        )
                    );
            }

            if (criteria.getAbsorbantName() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAbsorbantName(),
                            root -> root.join(Giver_.absorbant, JoinType.LEFT).get(User_.firstName)
                        )
                    );
            }
            if (criteria.getAbsorbantFamily() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAbsorbantFamily(),
                            root -> root.join(Giver_.absorbant, JoinType.LEFT).get(User_.lastName)
                        )
                    );
            }
            if (criteria.getProvinceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getProvinceId(), root -> root.join(Giver_.province, JoinType.LEFT).get(Province_.id))
                    );
            }
            if (criteria.getCityId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCityId(), root -> root.join(Giver_.city, JoinType.LEFT).get(City_.id))
                    );
            }
        }
        return specification;
    }
}
