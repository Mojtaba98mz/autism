package com.armaghanehayat.autism.service;

import com.armaghanehayat.autism.domain.*; // for static metamodels
import com.armaghanehayat.autism.domain.Province;
import com.armaghanehayat.autism.repository.ProvinceRepository;
import com.armaghanehayat.autism.service.criteria.ProvinceCriteria;
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
 * Service for executing complex queries for {@link Province} entities in the database.
 * The main input is a {@link ProvinceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Province} or a {@link Page} of {@link Province} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProvinceQueryService extends QueryService<Province> {

    private final Logger log = LoggerFactory.getLogger(ProvinceQueryService.class);

    private final ProvinceRepository provinceRepository;

    public ProvinceQueryService(ProvinceRepository provinceRepository) {
        this.provinceRepository = provinceRepository;
    }

    /**
     * Return a {@link List} of {@link Province} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Province> findByCriteria(ProvinceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Province> specification = createSpecification(criteria);
        return provinceRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Province} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Province> findByCriteria(ProvinceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Province> specification = createSpecification(criteria);
        return provinceRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProvinceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Province> specification = createSpecification(criteria);
        return provinceRepository.count(specification);
    }

    /**
     * Function to convert {@link ProvinceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Province> createSpecification(ProvinceCriteria criteria) {
        Specification<Province> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Province_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Province_.name));
            }
            if (criteria.getEnName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEnName(), Province_.enName));
            }
            if (criteria.getCityId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCityId(), root -> root.join(Province_.cities, JoinType.LEFT).get(City_.id))
                    );
            }
            if (criteria.getGiverId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getGiverId(), root -> root.join(Province_.givers, JoinType.LEFT).get(Giver_.id))
                    );
            }
        }
        return specification;
    }
}
