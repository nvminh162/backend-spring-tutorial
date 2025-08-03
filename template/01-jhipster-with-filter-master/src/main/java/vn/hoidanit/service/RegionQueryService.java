package vn.hoidanit.service;

import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;
import vn.hoidanit.domain.*; // for static metamodels
import vn.hoidanit.domain.Region;
import vn.hoidanit.repository.RegionRepository;
import vn.hoidanit.service.criteria.RegionCriteria;

/**
 * Service for executing complex queries for {@link Region} entities in the database.
 * The main input is a {@link RegionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Region} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RegionQueryService extends QueryService<Region> {

    private static final Logger LOG = LoggerFactory.getLogger(RegionQueryService.class);

    private final RegionRepository regionRepository;

    public RegionQueryService(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    /**
     * Return a {@link List} of {@link Region} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Region> findByCriteria(RegionCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<Region> specification = createSpecification(criteria);
        return regionRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RegionCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Region> specification = createSpecification(criteria);
        return regionRepository.count(specification);
    }

    /**
     * Function to convert {@link RegionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Region> createSpecification(RegionCriteria criteria) {
        Specification<Region> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Region_.id));
            }
            if (criteria.getRegionName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRegionName(), Region_.regionName));
            }
            if (criteria.getCountryId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCountryId(), root -> root.join(Region_.country, JoinType.LEFT).get(Country_.id))
                );
            }
        }
        return specification;
    }
}
