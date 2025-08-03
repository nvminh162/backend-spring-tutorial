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
import vn.hoidanit.domain.Department;
import vn.hoidanit.repository.DepartmentRepository;
import vn.hoidanit.service.criteria.DepartmentCriteria;

/**
 * Service for executing complex queries for {@link Department} entities in the database.
 * The main input is a {@link DepartmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Department} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DepartmentQueryService extends QueryService<Department> {

    private static final Logger LOG = LoggerFactory.getLogger(DepartmentQueryService.class);

    private final DepartmentRepository departmentRepository;

    public DepartmentQueryService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    /**
     * Return a {@link List} of {@link Department} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Department> findByCriteria(DepartmentCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<Department> specification = createSpecification(criteria);
        return departmentRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DepartmentCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Department> specification = createSpecification(criteria);
        return departmentRepository.count(specification);
    }

    /**
     * Function to convert {@link DepartmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Department> createSpecification(DepartmentCriteria criteria) {
        Specification<Department> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Department_.id));
            }
            if (criteria.getDepartmentName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDepartmentName(), Department_.departmentName));
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getLocationId(), root -> root.join(Department_.location, JoinType.LEFT).get(Location_.id))
                );
            }
            if (criteria.getEmployeeId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getEmployeeId(), root -> root.join(Department_.employees, JoinType.LEFT).get(Employee_.id))
                );
            }
            if (criteria.getJobHistoryId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getJobHistoryId(), root ->
                        root.join(Department_.jobHistory, JoinType.LEFT).get(JobHistory_.id)
                    )
                );
            }
        }
        return specification;
    }
}
