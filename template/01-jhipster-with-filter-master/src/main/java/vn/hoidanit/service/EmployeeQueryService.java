package vn.hoidanit.service;

import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;
import vn.hoidanit.domain.*; // for static metamodels
import vn.hoidanit.domain.Employee;
import vn.hoidanit.repository.EmployeeRepository;
import vn.hoidanit.service.criteria.EmployeeCriteria;

/**
 * Service for executing complex queries for {@link Employee} entities in the database.
 * The main input is a {@link EmployeeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Employee} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EmployeeQueryService extends QueryService<Employee> {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeQueryService.class);

    private final EmployeeRepository employeeRepository;

    public EmployeeQueryService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * Return a {@link Page} of {@link Employee} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Employee> findByCriteria(EmployeeCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Employee> specification = createSpecification(criteria);
        return employeeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EmployeeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Employee> specification = createSpecification(criteria);
        return employeeRepository.count(specification);
    }

    /**
     * Function to convert {@link EmployeeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Employee> createSpecification(EmployeeCriteria criteria) {
        Specification<Employee> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Employee_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), Employee_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Employee_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Employee_.email));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), Employee_.phoneNumber));
            }
            if (criteria.getHireDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHireDate(), Employee_.hireDate));
            }
            if (criteria.getSalary() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSalary(), Employee_.salary));
            }
            if (criteria.getCommissionPct() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCommissionPct(), Employee_.commissionPct));
            }
            if (criteria.getJobId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getJobId(), root -> root.join(Employee_.jobs, JoinType.LEFT).get(Job_.id))
                );
            }
            if (criteria.getManagerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getManagerId(), root -> root.join(Employee_.manager, JoinType.LEFT).get(Employee_.id))
                );
            }
            if (criteria.getDepartmentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getDepartmentId(), root ->
                        root.join(Employee_.department, JoinType.LEFT).get(Department_.id)
                    )
                );
            }
            if (criteria.getJobHistoryId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getJobHistoryId(), root ->
                        root.join(Employee_.jobHistory, JoinType.LEFT).get(JobHistory_.id)
                    )
                );
            }
        }
        return specification;
    }
}
