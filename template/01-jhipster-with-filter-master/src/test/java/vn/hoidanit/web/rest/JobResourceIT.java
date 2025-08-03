package vn.hoidanit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static vn.hoidanit.domain.JobAsserts.*;
import static vn.hoidanit.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import vn.hoidanit.IntegrationTest;
import vn.hoidanit.domain.Employee;
import vn.hoidanit.domain.Job;
import vn.hoidanit.domain.Task;
import vn.hoidanit.repository.JobRepository;
import vn.hoidanit.service.JobService;

/**
 * Integration tests for the {@link JobResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class JobResourceIT {

    private static final String DEFAULT_JOB_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_JOB_TITLE = "BBBBBBBBBB";

    private static final Long DEFAULT_MIN_SALARY = 1L;
    private static final Long UPDATED_MIN_SALARY = 2L;
    private static final Long SMALLER_MIN_SALARY = 1L - 1L;

    private static final Long DEFAULT_MAX_SALARY = 1L;
    private static final Long UPDATED_MAX_SALARY = 2L;
    private static final Long SMALLER_MAX_SALARY = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/jobs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private JobRepository jobRepository;

    @Mock
    private JobRepository jobRepositoryMock;

    @Mock
    private JobService jobServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restJobMockMvc;

    private Job job;

    private Job insertedJob;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Job createEntity() {
        return new Job().jobTitle(DEFAULT_JOB_TITLE).minSalary(DEFAULT_MIN_SALARY).maxSalary(DEFAULT_MAX_SALARY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Job createUpdatedEntity() {
        return new Job().jobTitle(UPDATED_JOB_TITLE).minSalary(UPDATED_MIN_SALARY).maxSalary(UPDATED_MAX_SALARY);
    }

    @BeforeEach
    public void initTest() {
        job = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedJob != null) {
            jobRepository.delete(insertedJob);
            insertedJob = null;
        }
    }

    @Test
    @Transactional
    void createJob() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Job
        var returnedJob = om.readValue(
            restJobMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(job)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Job.class
        );

        // Validate the Job in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertJobUpdatableFieldsEquals(returnedJob, getPersistedJob(returnedJob));

        insertedJob = returnedJob;
    }

    @Test
    @Transactional
    void createJobWithExistingId() throws Exception {
        // Create the Job with an existing ID
        job.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(job)))
            .andExpect(status().isBadRequest());

        // Validate the Job in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllJobs() throws Exception {
        // Initialize the database
        insertedJob = jobRepository.saveAndFlush(job);

        // Get all the jobList
        restJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(job.getId().intValue())))
            .andExpect(jsonPath("$.[*].jobTitle").value(hasItem(DEFAULT_JOB_TITLE)))
            .andExpect(jsonPath("$.[*].minSalary").value(hasItem(DEFAULT_MIN_SALARY.intValue())))
            .andExpect(jsonPath("$.[*].maxSalary").value(hasItem(DEFAULT_MAX_SALARY.intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllJobsWithEagerRelationshipsIsEnabled() throws Exception {
        when(jobServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restJobMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(jobServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllJobsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(jobServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restJobMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(jobRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getJob() throws Exception {
        // Initialize the database
        insertedJob = jobRepository.saveAndFlush(job);

        // Get the job
        restJobMockMvc
            .perform(get(ENTITY_API_URL_ID, job.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(job.getId().intValue()))
            .andExpect(jsonPath("$.jobTitle").value(DEFAULT_JOB_TITLE))
            .andExpect(jsonPath("$.minSalary").value(DEFAULT_MIN_SALARY.intValue()))
            .andExpect(jsonPath("$.maxSalary").value(DEFAULT_MAX_SALARY.intValue()));
    }

    @Test
    @Transactional
    void getJobsByIdFiltering() throws Exception {
        // Initialize the database
        insertedJob = jobRepository.saveAndFlush(job);

        Long id = job.getId();

        defaultJobFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultJobFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultJobFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllJobsByJobTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedJob = jobRepository.saveAndFlush(job);

        // Get all the jobList where jobTitle equals to
        defaultJobFiltering("jobTitle.equals=" + DEFAULT_JOB_TITLE, "jobTitle.equals=" + UPDATED_JOB_TITLE);
    }

    @Test
    @Transactional
    void getAllJobsByJobTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedJob = jobRepository.saveAndFlush(job);

        // Get all the jobList where jobTitle in
        defaultJobFiltering("jobTitle.in=" + DEFAULT_JOB_TITLE + "," + UPDATED_JOB_TITLE, "jobTitle.in=" + UPDATED_JOB_TITLE);
    }

    @Test
    @Transactional
    void getAllJobsByJobTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedJob = jobRepository.saveAndFlush(job);

        // Get all the jobList where jobTitle is not null
        defaultJobFiltering("jobTitle.specified=true", "jobTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllJobsByJobTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedJob = jobRepository.saveAndFlush(job);

        // Get all the jobList where jobTitle contains
        defaultJobFiltering("jobTitle.contains=" + DEFAULT_JOB_TITLE, "jobTitle.contains=" + UPDATED_JOB_TITLE);
    }

    @Test
    @Transactional
    void getAllJobsByJobTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedJob = jobRepository.saveAndFlush(job);

        // Get all the jobList where jobTitle does not contain
        defaultJobFiltering("jobTitle.doesNotContain=" + UPDATED_JOB_TITLE, "jobTitle.doesNotContain=" + DEFAULT_JOB_TITLE);
    }

    @Test
    @Transactional
    void getAllJobsByMinSalaryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedJob = jobRepository.saveAndFlush(job);

        // Get all the jobList where minSalary equals to
        defaultJobFiltering("minSalary.equals=" + DEFAULT_MIN_SALARY, "minSalary.equals=" + UPDATED_MIN_SALARY);
    }

    @Test
    @Transactional
    void getAllJobsByMinSalaryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedJob = jobRepository.saveAndFlush(job);

        // Get all the jobList where minSalary in
        defaultJobFiltering("minSalary.in=" + DEFAULT_MIN_SALARY + "," + UPDATED_MIN_SALARY, "minSalary.in=" + UPDATED_MIN_SALARY);
    }

    @Test
    @Transactional
    void getAllJobsByMinSalaryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedJob = jobRepository.saveAndFlush(job);

        // Get all the jobList where minSalary is not null
        defaultJobFiltering("minSalary.specified=true", "minSalary.specified=false");
    }

    @Test
    @Transactional
    void getAllJobsByMinSalaryIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedJob = jobRepository.saveAndFlush(job);

        // Get all the jobList where minSalary is greater than or equal to
        defaultJobFiltering("minSalary.greaterThanOrEqual=" + DEFAULT_MIN_SALARY, "minSalary.greaterThanOrEqual=" + UPDATED_MIN_SALARY);
    }

    @Test
    @Transactional
    void getAllJobsByMinSalaryIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedJob = jobRepository.saveAndFlush(job);

        // Get all the jobList where minSalary is less than or equal to
        defaultJobFiltering("minSalary.lessThanOrEqual=" + DEFAULT_MIN_SALARY, "minSalary.lessThanOrEqual=" + SMALLER_MIN_SALARY);
    }

    @Test
    @Transactional
    void getAllJobsByMinSalaryIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedJob = jobRepository.saveAndFlush(job);

        // Get all the jobList where minSalary is less than
        defaultJobFiltering("minSalary.lessThan=" + UPDATED_MIN_SALARY, "minSalary.lessThan=" + DEFAULT_MIN_SALARY);
    }

    @Test
    @Transactional
    void getAllJobsByMinSalaryIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedJob = jobRepository.saveAndFlush(job);

        // Get all the jobList where minSalary is greater than
        defaultJobFiltering("minSalary.greaterThan=" + SMALLER_MIN_SALARY, "minSalary.greaterThan=" + DEFAULT_MIN_SALARY);
    }

    @Test
    @Transactional
    void getAllJobsByMaxSalaryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedJob = jobRepository.saveAndFlush(job);

        // Get all the jobList where maxSalary equals to
        defaultJobFiltering("maxSalary.equals=" + DEFAULT_MAX_SALARY, "maxSalary.equals=" + UPDATED_MAX_SALARY);
    }

    @Test
    @Transactional
    void getAllJobsByMaxSalaryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedJob = jobRepository.saveAndFlush(job);

        // Get all the jobList where maxSalary in
        defaultJobFiltering("maxSalary.in=" + DEFAULT_MAX_SALARY + "," + UPDATED_MAX_SALARY, "maxSalary.in=" + UPDATED_MAX_SALARY);
    }

    @Test
    @Transactional
    void getAllJobsByMaxSalaryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedJob = jobRepository.saveAndFlush(job);

        // Get all the jobList where maxSalary is not null
        defaultJobFiltering("maxSalary.specified=true", "maxSalary.specified=false");
    }

    @Test
    @Transactional
    void getAllJobsByMaxSalaryIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedJob = jobRepository.saveAndFlush(job);

        // Get all the jobList where maxSalary is greater than or equal to
        defaultJobFiltering("maxSalary.greaterThanOrEqual=" + DEFAULT_MAX_SALARY, "maxSalary.greaterThanOrEqual=" + UPDATED_MAX_SALARY);
    }

    @Test
    @Transactional
    void getAllJobsByMaxSalaryIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedJob = jobRepository.saveAndFlush(job);

        // Get all the jobList where maxSalary is less than or equal to
        defaultJobFiltering("maxSalary.lessThanOrEqual=" + DEFAULT_MAX_SALARY, "maxSalary.lessThanOrEqual=" + SMALLER_MAX_SALARY);
    }

    @Test
    @Transactional
    void getAllJobsByMaxSalaryIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedJob = jobRepository.saveAndFlush(job);

        // Get all the jobList where maxSalary is less than
        defaultJobFiltering("maxSalary.lessThan=" + UPDATED_MAX_SALARY, "maxSalary.lessThan=" + DEFAULT_MAX_SALARY);
    }

    @Test
    @Transactional
    void getAllJobsByMaxSalaryIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedJob = jobRepository.saveAndFlush(job);

        // Get all the jobList where maxSalary is greater than
        defaultJobFiltering("maxSalary.greaterThan=" + SMALLER_MAX_SALARY, "maxSalary.greaterThan=" + DEFAULT_MAX_SALARY);
    }

    @Test
    @Transactional
    void getAllJobsByTaskIsEqualToSomething() throws Exception {
        Task task;
        if (TestUtil.findAll(em, Task.class).isEmpty()) {
            jobRepository.saveAndFlush(job);
            task = TaskResourceIT.createEntity();
        } else {
            task = TestUtil.findAll(em, Task.class).get(0);
        }
        em.persist(task);
        em.flush();
        job.addTask(task);
        jobRepository.saveAndFlush(job);
        Long taskId = task.getId();
        // Get all the jobList where task equals to taskId
        defaultJobShouldBeFound("taskId.equals=" + taskId);

        // Get all the jobList where task equals to (taskId + 1)
        defaultJobShouldNotBeFound("taskId.equals=" + (taskId + 1));
    }

    @Test
    @Transactional
    void getAllJobsByEmployeeIsEqualToSomething() throws Exception {
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            jobRepository.saveAndFlush(job);
            employee = EmployeeResourceIT.createEntity();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        em.persist(employee);
        em.flush();
        job.setEmployee(employee);
        jobRepository.saveAndFlush(job);
        Long employeeId = employee.getId();
        // Get all the jobList where employee equals to employeeId
        defaultJobShouldBeFound("employeeId.equals=" + employeeId);

        // Get all the jobList where employee equals to (employeeId + 1)
        defaultJobShouldNotBeFound("employeeId.equals=" + (employeeId + 1));
    }

    private void defaultJobFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultJobShouldBeFound(shouldBeFound);
        defaultJobShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultJobShouldBeFound(String filter) throws Exception {
        restJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(job.getId().intValue())))
            .andExpect(jsonPath("$.[*].jobTitle").value(hasItem(DEFAULT_JOB_TITLE)))
            .andExpect(jsonPath("$.[*].minSalary").value(hasItem(DEFAULT_MIN_SALARY.intValue())))
            .andExpect(jsonPath("$.[*].maxSalary").value(hasItem(DEFAULT_MAX_SALARY.intValue())));

        // Check, that the count call also returns 1
        restJobMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultJobShouldNotBeFound(String filter) throws Exception {
        restJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restJobMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingJob() throws Exception {
        // Get the job
        restJobMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingJob() throws Exception {
        // Initialize the database
        insertedJob = jobRepository.saveAndFlush(job);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the job
        Job updatedJob = jobRepository.findById(job.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedJob are not directly saved in db
        em.detach(updatedJob);
        updatedJob.jobTitle(UPDATED_JOB_TITLE).minSalary(UPDATED_MIN_SALARY).maxSalary(UPDATED_MAX_SALARY);

        restJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedJob.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(updatedJob))
            )
            .andExpect(status().isOk());

        // Validate the Job in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedJobToMatchAllProperties(updatedJob);
    }

    @Test
    @Transactional
    void putNonExistingJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        job.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJobMockMvc
            .perform(put(ENTITY_API_URL_ID, job.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(job)))
            .andExpect(status().isBadRequest());

        // Validate the Job in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        job.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(job))
            )
            .andExpect(status().isBadRequest());

        // Validate the Job in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        job.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(job)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Job in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateJobWithPatch() throws Exception {
        // Initialize the database
        insertedJob = jobRepository.saveAndFlush(job);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the job using partial update
        Job partialUpdatedJob = new Job();
        partialUpdatedJob.setId(job.getId());

        partialUpdatedJob.jobTitle(UPDATED_JOB_TITLE).maxSalary(UPDATED_MAX_SALARY);

        restJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJob.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedJob))
            )
            .andExpect(status().isOk());

        // Validate the Job in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertJobUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedJob, job), getPersistedJob(job));
    }

    @Test
    @Transactional
    void fullUpdateJobWithPatch() throws Exception {
        // Initialize the database
        insertedJob = jobRepository.saveAndFlush(job);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the job using partial update
        Job partialUpdatedJob = new Job();
        partialUpdatedJob.setId(job.getId());

        partialUpdatedJob.jobTitle(UPDATED_JOB_TITLE).minSalary(UPDATED_MIN_SALARY).maxSalary(UPDATED_MAX_SALARY);

        restJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJob.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedJob))
            )
            .andExpect(status().isOk());

        // Validate the Job in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertJobUpdatableFieldsEquals(partialUpdatedJob, getPersistedJob(partialUpdatedJob));
    }

    @Test
    @Transactional
    void patchNonExistingJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        job.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJobMockMvc
            .perform(patch(ENTITY_API_URL_ID, job.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(job)))
            .andExpect(status().isBadRequest());

        // Validate the Job in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        job.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(job))
            )
            .andExpect(status().isBadRequest());

        // Validate the Job in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        job.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(job)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Job in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteJob() throws Exception {
        // Initialize the database
        insertedJob = jobRepository.saveAndFlush(job);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the job
        restJobMockMvc.perform(delete(ENTITY_API_URL_ID, job.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return jobRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Job getPersistedJob(Job job) {
        return jobRepository.findById(job.getId()).orElseThrow();
    }

    protected void assertPersistedJobToMatchAllProperties(Job expectedJob) {
        assertJobAllPropertiesEquals(expectedJob, getPersistedJob(expectedJob));
    }

    protected void assertPersistedJobToMatchUpdatableProperties(Job expectedJob) {
        assertJobAllUpdatablePropertiesEquals(expectedJob, getPersistedJob(expectedJob));
    }
}
