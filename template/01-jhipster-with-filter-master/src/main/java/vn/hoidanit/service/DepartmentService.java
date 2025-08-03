package vn.hoidanit.service;

import java.util.List;
import java.util.Optional;
import vn.hoidanit.domain.Department;

/**
 * Service Interface for managing {@link vn.hoidanit.domain.Department}.
 */
public interface DepartmentService {
    /**
     * Save a department.
     *
     * @param department the entity to save.
     * @return the persisted entity.
     */
    Department save(Department department);

    /**
     * Updates a department.
     *
     * @param department the entity to update.
     * @return the persisted entity.
     */
    Department update(Department department);

    /**
     * Partially updates a department.
     *
     * @param department the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Department> partialUpdate(Department department);

    /**
     * Get all the Department where JobHistory is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Department> findAllWhereJobHistoryIsNull();

    /**
     * Get the "id" department.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Department> findOne(Long id);

    /**
     * Delete the "id" department.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
