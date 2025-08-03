package vn.hoidanit.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.hoidanit.domain.Job;
import vn.hoidanit.repository.JobRepository;

/**
 * Service Implementation for managing {@link vn.hoidanit.domain.Job}.
 */
@Service
@Transactional
public class JobService {

    private static final Logger LOG = LoggerFactory.getLogger(JobService.class);

    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    /**
     * Save a job.
     *
     * @param job the entity to save.
     * @return the persisted entity.
     */
    public Job save(Job job) {
        LOG.debug("Request to save Job : {}", job);
        return jobRepository.save(job);
    }

    /**
     * Update a job.
     *
     * @param job the entity to save.
     * @return the persisted entity.
     */
    public Job update(Job job) {
        LOG.debug("Request to update Job : {}", job);
        return jobRepository.save(job);
    }

    /**
     * Partially update a job.
     *
     * @param job the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Job> partialUpdate(Job job) {
        LOG.debug("Request to partially update Job : {}", job);

        return jobRepository
            .findById(job.getId())
            .map(existingJob -> {
                if (job.getJobTitle() != null) {
                    existingJob.setJobTitle(job.getJobTitle());
                }
                if (job.getMinSalary() != null) {
                    existingJob.setMinSalary(job.getMinSalary());
                }
                if (job.getMaxSalary() != null) {
                    existingJob.setMaxSalary(job.getMaxSalary());
                }

                return existingJob;
            })
            .map(jobRepository::save);
    }

    /**
     * Get all the jobs with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Job> findAllWithEagerRelationships(Pageable pageable) {
        return jobRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     *  Get all the jobs where JobHistory is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Job> findAllWhereJobHistoryIsNull() {
        LOG.debug("Request to get all jobs where JobHistory is null");
        return StreamSupport.stream(jobRepository.findAll().spliterator(), false).filter(job -> job.getJobHistory() == null).toList();
    }

    /**
     * Get one job by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Job> findOne(Long id) {
        LOG.debug("Request to get Job : {}", id);
        return jobRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the job by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Job : {}", id);
        jobRepository.deleteById(id);
    }
}
