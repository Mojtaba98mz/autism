package com.armaghanehayat.autism.service;

import com.armaghanehayat.autism.domain.*;
import com.armaghanehayat.autism.repository.GiverRepository;
import com.armaghanehayat.autism.repository.UserRepository;
import com.armaghanehayat.autism.service.criteria.GiverCriteria;
import com.armaghanehayat.autism.service.criteria.UserCriteria;
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
import tech.jhipster.service.filter.LongFilter;

/**
 * Service for executing complex queries for {@link Giver} entities in the database.
 * The main input is a {@link GiverCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Giver} or a {@link Page} of {@link Giver} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserQueryService extends QueryService<User> {

    private final Logger log = LoggerFactory.getLogger(UserQueryService.class);

    private final UserRepository userRepository;

    private final UserService userService;

    public UserQueryService(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    /**
     * Return a {@link List} of {@link Giver} which matches the criteria from the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    /*@Transactional(readOnly = true)
    public List<Giver> findByCriteria(GiverCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Giver> specification = createSpecification(criteria);
        return giverRepository.findAll(specification);
    }*/

    /**
     * Return a {@link Page} of {@link Giver} which matches the criteria from the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<User> findByCriteria(UserCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<User> specification = createSpecification(criteria);
        return userRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    /*@Transactional(readOnly = true)
    public long countByCriteria(GiverCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Giver> specification = createSpecification(criteria);
        return giverRepository.count(specification);
    }*/

    /**
     * Function to convert {@link GiverCriteria} to a {@link Specification}
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<User> createSpecification(UserCriteria criteria) {
        Specification<User> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), User_.id));
            }
            if (criteria.getLogin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLogin(), User_.login));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), User_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), User_.lastName));
            }
        }
        return specification;
    }
}
