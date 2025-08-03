package vn.hoidanit.service;

import java.util.Optional;
import vn.hoidanit.domain.JobHistory;

/**
 * Service Interface for managing {@link vn.hoidanit.domain.JobHistory}.
 */
public interface JobHistoryService {
    /**
     * Save a jobHistory.
     *
     * @param jobHistory the entity to save.
     * @return the persisted entity.
     */
    JobHistory save(JobHistory jobHistory);

    /**
     * Updates a jobHistory.
     *
     * @param jobHistory the entity to update.
     * @return the persisted entity.
     */
    JobHistory update(JobHistory jobHistory);

    /**
     * Partially updates a jobHistory.
     *
     * @param jobHistory the entity to update partially.
     * @return the persisted entity.
     */
    Optional<JobHistory> partialUpdate(JobHistory jobHistory);

    /**
     * Get the "id" jobHistory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<JobHistory> findOne(Long id);

    /**
     * Delete the "id" jobHistory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
