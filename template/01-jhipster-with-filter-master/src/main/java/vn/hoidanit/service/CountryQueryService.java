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
import vn.hoidanit.domain.Country;
import vn.hoidanit.repository.CountryRepository;
import vn.hoidanit.service.criteria.CountryCriteria;

/**
 * Service for executing complex queries for {@link Country} entities in the database.
 * The main input is a {@link CountryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Country} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CountryQueryService extends QueryService<Country> {

    private static final Logger LOG = LoggerFactory.getLogger(CountryQueryService.class);

    private final CountryRepository countryRepository;

    public CountryQueryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    /**
     * Return a {@link List} of {@link Country} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Country> findByCriteria(CountryCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<Country> specification = createSpecification(criteria);
        return countryRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CountryCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Country> specification = createSpecification(criteria);
        return countryRepository.count(specification);
    }

    /**
     * Function to convert {@link CountryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Country> createSpecification(CountryCriteria criteria) {
        Specification<Country> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Country_.id));
            }
            if (criteria.getCountryName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountryName(), Country_.countryName));
            }
            if (criteria.getRegionId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getRegionId(), root -> root.join(Country_.region, JoinType.LEFT).get(Region_.id))
                );
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getLocationId(), root -> root.join(Country_.location, JoinType.LEFT).get(Location_.id))
                );
            }
        }
        return specification;
    }
}
