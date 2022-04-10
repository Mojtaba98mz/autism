package com.armaghanehayat.autism.service;

import com.armaghanehayat.autism.domain.*; // for static metamodels
import com.armaghanehayat.autism.domain.Donation;
import com.armaghanehayat.autism.repository.DonationRepository;
import com.armaghanehayat.autism.service.criteria.DonationCriteria;
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
 * Service for executing complex queries for {@link Donation} entities in the database.
 * The main input is a {@link DonationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Donation} or a {@link Page} of {@link Donation} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DonationQueryService extends QueryService<Donation> {

    private final Logger log = LoggerFactory.getLogger(DonationQueryService.class);

    private final DonationRepository donationRepository;

    public DonationQueryService(DonationRepository donationRepository) {
        this.donationRepository = donationRepository;
    }

    /**
     * Return a {@link List} of {@link Donation} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Donation> findByCriteria(DonationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Donation> specification = createSpecification(criteria);
        return donationRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Donation} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Donation> findByCriteria(DonationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Donation> specification = createSpecification(criteria);
        return donationRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DonationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Donation> specification = createSpecification(criteria);
        return donationRepository.count(specification);
    }

    /**
     * Function to convert {@link DonationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Donation> createSpecification(DonationCriteria criteria) {
        Specification<Donation> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Donation_.id));
            }
            if (criteria.getIsCash() != null) {
                specification = specification.and(buildSpecification(criteria.getIsCash(), Donation_.isCash));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), Donation_.amount));
            }
            if (criteria.getDonationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDonationDate(), Donation_.donationDate));
            }
            if (criteria.getHelpType() != null) {
                specification = specification.and(buildSpecification(criteria.getHelpType(), Donation_.helpType));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Donation_.description));
            }
            if (criteria.getAccount() != null) {
                specification = specification.and(buildSpecification(criteria.getAccount(), Donation_.account));
            }
            if (criteria.getRegisterDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRegisterDate(), Donation_.registerDate));
            }
            if (criteria.getGiverId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getGiverId(), root -> root.join(Donation_.giver, JoinType.LEFT).get(Giver_.id))
                    );
            }
        }
        return specification;
    }
}
