package vn.hoidanit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static vn.hoidanit.domain.JobHistoryAsserts.*;
import static vn.hoidanit.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import vn.hoidanit.IntegrationTest;
import vn.hoidanit.domain.Department;
import vn.hoidanit.domain.Employee;
import vn.hoidanit.domain.Job;
import vn.hoidanit.domain.JobHistory;
import vn.hoidanit.domain.enumeration.Language;
import vn.hoidanit.repository.JobHistoryRepository;

/**
 * Integration tests for the {@link JobHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class JobHistoryResourceIT {

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Language DEFAULT_LANGUAGE = Language.FRENCH;
    private static final Language UPDATED_LANGUAGE = Language.ENGLISH;

    private static final String ENTITY_API_URL = "/api/job-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private JobHistoryRepository jobHistoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restJobHistoryMockMvc;

    private JobHistory jobHistory;

    private JobHistory insertedJobHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobHistory createEntity() {
        return new JobHistory().startDate(DEFAULT_START_DATE).endDate(DEFAULT_END_DATE).language(DEFAULT_LANGUAGE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobHistory createUpdatedEntity() {
        return new JobHistory().startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE).language(UPDATED_LANGUAGE);
    }

    @BeforeEach
    public void initTest() {
        jobHistory = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedJobHistory != null) {
            jobHistoryRepository.delete(insertedJobHistory);
            insertedJobHistory = null;
        }
    }

    @Test
    @Transactional
    void createJobHistory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the JobHistory
        var returnedJobHistory = om.readValue(
            restJobHistoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(jobHistory)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            JobHistory.class
        );

        // Validate the JobHistory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertJobHistoryUpdatableFieldsEquals(returnedJobHistory, getPersistedJobHistory(returnedJobHistory));

        insertedJobHistory = returnedJobHistory;
    }

    @Test
    @Transactional
    void createJobHistoryWithExistingId() throws Exception {
        // Create the JobHistory with an existing ID
        jobHistory.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(jobHistory)))
            .andExpect(status().isBadRequest());

        // Validate the JobHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllJobHistories() throws Exception {
        // Initialize the database
        insertedJobHistory = jobHistoryRepository.saveAndFlush(jobHistory);

        // Get all the jobHistoryList
        restJobHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())));
    }

    @Test
    @Transactional
    void getJobHistory() throws Exception {
        // Initialize the database
        insertedJobHistory = jobHistoryRepository.saveAndFlush(jobHistory);

        // Get the jobHistory
        restJobHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, jobHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(jobHistory.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()));
    }

    @Test
    @Transactional
    void getJobHistoriesByIdFiltering() throws Exception {
        // Initialize the database
        insertedJobHistory = jobHistoryRepository.saveAndFlush(jobHistory);

        Long id = jobHistory.getId();

        defaultJobHistoryFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultJobHistoryFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultJobHistoryFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllJobHistoriesByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedJobHistory = jobHistoryRepository.saveAndFlush(jobHistory);

        // Get all the jobHistoryList where startDate equals to
        defaultJobHistoryFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllJobHistoriesByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedJobHistory = jobHistoryRepository.saveAndFlush(jobHistory);

        // Get all the jobHistoryList where startDate in
        defaultJobHistoryFiltering("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE, "startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllJobHistoriesByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedJobHistory = jobHistoryRepository.saveAndFlush(jobHistory);

        // Get all the jobHistoryList where startDate is not null
        defaultJobHistoryFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllJobHistoriesByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedJobHistory = jobHistoryRepository.saveAndFlush(jobHistory);

        // Get all the jobHistoryList where endDate equals to
        defaultJobHistoryFiltering("endDate.equals=" + DEFAULT_END_DATE, "endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllJobHistoriesByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedJobHistory = jobHistoryRepository.saveAndFlush(jobHistory);

        // Get all the jobHistoryList where endDate in
        defaultJobHistoryFiltering("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE, "endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllJobHistoriesByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedJobHistory = jobHistoryRepository.saveAndFlush(jobHistory);

        // Get all the jobHistoryList where endDate is not null
        defaultJobHistoryFiltering("endDate.specified=true", "endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllJobHistoriesByLanguageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedJobHistory = jobHistoryRepository.saveAndFlush(jobHistory);

        // Get all the jobHistoryList where language equals to
        defaultJobHistoryFiltering("language.equals=" + DEFAULT_LANGUAGE, "language.equals=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllJobHistoriesByLanguageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedJobHistory = jobHistoryRepository.saveAndFlush(jobHistory);

        // Get all the jobHistoryList where language in
        defaultJobHistoryFiltering("language.in=" + DEFAULT_LANGUAGE + "," + UPDATED_LANGUAGE, "language.in=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllJobHistoriesByLanguageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedJobHistory = jobHistoryRepository.saveAndFlush(jobHistory);

        // Get all the jobHistoryList where language is not null
        defaultJobHistoryFiltering("language.specified=true", "language.specified=false");
    }

    @Test
    @Transactional
    void getAllJobHistoriesByJobIsEqualToSomething() throws Exception {
        Job job;
        if (TestUtil.findAll(em, Job.class).isEmpty()) {
            jobHistoryRepository.saveAndFlush(jobHistory);
            job = JobResourceIT.createEntity();
        } else {
            job = TestUtil.findAll(em, Job.class).get(0);
        }
        em.persist(job);
        em.flush();
        jobHistory.setJob(job);
        jobHistoryRepository.saveAndFlush(jobHistory);
        Long jobId = job.getId();
        // Get all the jobHistoryList where job equals to jobId
        defaultJobHistoryShouldBeFound("jobId.equals=" + jobId);

        // Get all the jobHistoryList where job equals to (jobId + 1)
        defaultJobHistoryShouldNotBeFound("jobId.equals=" + (jobId + 1));
    }

    @Test
    @Transactional
    void getAllJobHistoriesByDepartmentIsEqualToSomething() throws Exception {
        Department department;
        if (TestUtil.findAll(em, Department.class).isEmpty()) {
            jobHistoryRepository.saveAndFlush(jobHistory);
            department = DepartmentResourceIT.createEntity();
        } else {
            department = TestUtil.findAll(em, Department.class).get(0);
        }
        em.persist(department);
        em.flush();
        jobHistory.setDepartment(department);
        jobHistoryRepository.saveAndFlush(jobHistory);
        Long departmentId = department.getId();
        // Get all the jobHistoryList where department equals to departmentId
        defaultJobHistoryShouldBeFound("departmentId.equals=" + departmentId);

        // Get all the jobHistoryList where department equals to (departmentId + 1)
        defaultJobHistoryShouldNotBeFound("departmentId.equals=" + (departmentId + 1));
    }

    @Test
    @Transactional
    void getAllJobHistoriesByEmployeeIsEqualToSomething() throws Exception {
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            jobHistoryRepository.saveAndFlush(jobHistory);
            employee = EmployeeResourceIT.createEntity();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        em.persist(employee);
        em.flush();
        jobHistory.setEmployee(employee);
        jobHistoryRepository.saveAndFlush(jobHistory);
        Long employeeId = employee.getId();
        // Get all the jobHistoryList where employee equals to employeeId
        defaultJobHistoryShouldBeFound("employeeId.equals=" + employeeId);

        // Get all the jobHistoryList where employee equals to (employeeId + 1)
        defaultJobHistoryShouldNotBeFound("employeeId.equals=" + (employeeId + 1));
    }

    private void defaultJobHistoryFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultJobHistoryShouldBeFound(shouldBeFound);
        defaultJobHistoryShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultJobHistoryShouldBeFound(String filter) throws Exception {
        restJobHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())));

        // Check, that the count call also returns 1
        restJobHistoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultJobHistoryShouldNotBeFound(String filter) throws Exception {
        restJobHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restJobHistoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingJobHistory() throws Exception {
        // Get the jobHistory
        restJobHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingJobHistory() throws Exception {
        // Initialize the database
        insertedJobHistory = jobHistoryRepository.saveAndFlush(jobHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the jobHistory
        JobHistory updatedJobHistory = jobHistoryRepository.findById(jobHistory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedJobHistory are not directly saved in db
        em.detach(updatedJobHistory);
        updatedJobHistory.startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE).language(UPDATED_LANGUAGE);

        restJobHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedJobHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedJobHistory))
            )
            .andExpect(status().isOk());

        // Validate the JobHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedJobHistoryToMatchAllProperties(updatedJobHistory);
    }

    @Test
    @Transactional
    void putNonExistingJobHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        jobHistory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJobHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, jobHistory.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(jobHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchJobHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        jobHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(jobHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamJobHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        jobHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobHistoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(jobHistory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the JobHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateJobHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedJobHistory = jobHistoryRepository.saveAndFlush(jobHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the jobHistory using partial update
        JobHistory partialUpdatedJobHistory = new JobHistory();
        partialUpdatedJobHistory.setId(jobHistory.getId());

        partialUpdatedJobHistory.language(UPDATED_LANGUAGE);

        restJobHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJobHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedJobHistory))
            )
            .andExpect(status().isOk());

        // Validate the JobHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertJobHistoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedJobHistory, jobHistory),
            getPersistedJobHistory(jobHistory)
        );
    }

    @Test
    @Transactional
    void fullUpdateJobHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedJobHistory = jobHistoryRepository.saveAndFlush(jobHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the jobHistory using partial update
        JobHistory partialUpdatedJobHistory = new JobHistory();
        partialUpdatedJobHistory.setId(jobHistory.getId());

        partialUpdatedJobHistory.startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE).language(UPDATED_LANGUAGE);

        restJobHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJobHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedJobHistory))
            )
            .andExpect(status().isOk());

        // Validate the JobHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertJobHistoryUpdatableFieldsEquals(partialUpdatedJobHistory, getPersistedJobHistory(partialUpdatedJobHistory));
    }

    @Test
    @Transactional
    void patchNonExistingJobHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        jobHistory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJobHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, jobHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(jobHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchJobHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        jobHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(jobHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamJobHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        jobHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobHistoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(jobHistory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the JobHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteJobHistory() throws Exception {
        // Initialize the database
        insertedJobHistory = jobHistoryRepository.saveAndFlush(jobHistory);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the jobHistory
        restJobHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, jobHistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return jobHistoryRepository.count();
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

    protected JobHistory getPersistedJobHistory(JobHistory jobHistory) {
        return jobHistoryRepository.findById(jobHistory.getId()).orElseThrow();
    }

    protected void assertPersistedJobHistoryToMatchAllProperties(JobHistory expectedJobHistory) {
        assertJobHistoryAllPropertiesEquals(expectedJobHistory, getPersistedJobHistory(expectedJobHistory));
    }

    protected void assertPersistedJobHistoryToMatchUpdatableProperties(JobHistory expectedJobHistory) {
        assertJobHistoryAllUpdatablePropertiesEquals(expectedJobHistory, getPersistedJobHistory(expectedJobHistory));
    }
}
