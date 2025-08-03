package vn.hoidanit.service;

import java.util.Optional;
import vn.hoidanit.domain.Task;

/**
 * Service Interface for managing {@link vn.hoidanit.domain.Task}.
 */
public interface TaskService {
    /**
     * Save a task.
     *
     * @param task the entity to save.
     * @return the persisted entity.
     */
    Task save(Task task);

    /**
     * Updates a task.
     *
     * @param task the entity to update.
     * @return the persisted entity.
     */
    Task update(Task task);

    /**
     * Partially updates a task.
     *
     * @param task the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Task> partialUpdate(Task task);

    /**
     * Get the "id" task.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Task> findOne(Long id);

    /**
     * Delete the "id" task.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
