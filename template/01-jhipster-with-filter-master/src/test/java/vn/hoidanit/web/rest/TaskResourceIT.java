package vn.hoidanit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static vn.hoidanit.domain.TaskAsserts.*;
import static vn.hoidanit.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
import vn.hoidanit.domain.Job;
import vn.hoidanit.domain.Task;
import vn.hoidanit.repository.TaskRepository;

/**
 * Integration tests for the {@link TaskResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TaskResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tasks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTaskMockMvc;

    private Task task;

    private Task insertedTask;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Task createEntity() {
        return new Task().title(DEFAULT_TITLE).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Task createUpdatedEntity() {
        return new Task().title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        task = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedTask != null) {
            taskRepository.delete(insertedTask);
            insertedTask = null;
        }
    }

    @Test
    @Transactional
    void createTask() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Task
        var returnedTask = om.readValue(
            restTaskMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(task)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Task.class
        );

        // Validate the Task in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTaskUpdatableFieldsEquals(returnedTask, getPersistedTask(returnedTask));

        insertedTask = returnedTask;
    }

    @Test
    @Transactional
    void createTaskWithExistingId() throws Exception {
        // Create the Task with an existing ID
        task.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(task)))
            .andExpect(status().isBadRequest());

        // Validate the Task in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTasks() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        // Get all the taskList
        restTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(task.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getTask() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        // Get the task
        restTaskMockMvc
            .perform(get(ENTITY_API_URL_ID, task.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(task.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getTasksByIdFiltering() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        Long id = task.getId();

        defaultTaskFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTaskFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTaskFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTasksByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        // Get all the taskList where title equals to
        defaultTaskFiltering("title.equals=" + DEFAULT_TITLE, "title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllTasksByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        // Get all the taskList where title in
        defaultTaskFiltering("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE, "title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllTasksByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        // Get all the taskList where title is not null
        defaultTaskFiltering("title.specified=true", "title.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        // Get all the taskList where title contains
        defaultTaskFiltering("title.contains=" + DEFAULT_TITLE, "title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllTasksByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        // Get all the taskList where title does not contain
        defaultTaskFiltering("title.doesNotContain=" + UPDATED_TITLE, "title.doesNotContain=" + DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void getAllTasksByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        // Get all the taskList where description equals to
        defaultTaskFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTasksByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        // Get all the taskList where description in
        defaultTaskFiltering("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION, "description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTasksByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        // Get all the taskList where description is not null
        defaultTaskFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        // Get all the taskList where description contains
        defaultTaskFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTasksByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        // Get all the taskList where description does not contain
        defaultTaskFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTasksByJobIsEqualToSomething() throws Exception {
        Job job;
        if (TestUtil.findAll(em, Job.class).isEmpty()) {
            taskRepository.saveAndFlush(task);
            job = JobResourceIT.createEntity();
        } else {
            job = TestUtil.findAll(em, Job.class).get(0);
        }
        em.persist(job);
        em.flush();
        task.addJob(job);
        taskRepository.saveAndFlush(task);
        Long jobId = job.getId();
        // Get all the taskList where job equals to jobId
        defaultTaskShouldBeFound("jobId.equals=" + jobId);

        // Get all the taskList where job equals to (jobId + 1)
        defaultTaskShouldNotBeFound("jobId.equals=" + (jobId + 1));
    }

    private void defaultTaskFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTaskShouldBeFound(shouldBeFound);
        defaultTaskShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTaskShouldBeFound(String filter) throws Exception {
        restTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(task.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restTaskMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTaskShouldNotBeFound(String filter) throws Exception {
        restTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTaskMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTask() throws Exception {
        // Get the task
        restTaskMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTask() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the task
        Task updatedTask = taskRepository.findById(task.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTask are not directly saved in db
        em.detach(updatedTask);
        updatedTask.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);

        restTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTask.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTask))
            )
            .andExpect(status().isOk());

        // Validate the Task in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTaskToMatchAllProperties(updatedTask);
    }

    @Test
    @Transactional
    void putNonExistingTask() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        task.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskMockMvc
            .perform(put(ENTITY_API_URL_ID, task.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(task)))
            .andExpect(status().isBadRequest());

        // Validate the Task in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTask() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        task.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(task))
            )
            .andExpect(status().isBadRequest());

        // Validate the Task in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTask() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        task.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(task)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Task in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTaskWithPatch() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the task using partial update
        Task partialUpdatedTask = new Task();
        partialUpdatedTask.setId(task.getId());

        restTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTask.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTask))
            )
            .andExpect(status().isOk());

        // Validate the Task in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTaskUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedTask, task), getPersistedTask(task));
    }

    @Test
    @Transactional
    void fullUpdateTaskWithPatch() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the task using partial update
        Task partialUpdatedTask = new Task();
        partialUpdatedTask.setId(task.getId());

        partialUpdatedTask.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);

        restTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTask.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTask))
            )
            .andExpect(status().isOk());

        // Validate the Task in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTaskUpdatableFieldsEquals(partialUpdatedTask, getPersistedTask(partialUpdatedTask));
    }

    @Test
    @Transactional
    void patchNonExistingTask() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        task.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskMockMvc
            .perform(patch(ENTITY_API_URL_ID, task.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(task)))
            .andExpect(status().isBadRequest());

        // Validate the Task in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTask() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        task.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(task))
            )
            .andExpect(status().isBadRequest());

        // Validate the Task in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTask() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        task.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(task)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Task in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTask() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the task
        restTaskMockMvc
            .perform(delete(ENTITY_API_URL_ID, task.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return taskRepository.count();
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

    protected Task getPersistedTask(Task task) {
        return taskRepository.findById(task.getId()).orElseThrow();
    }

    protected void assertPersistedTaskToMatchAllProperties(Task expectedTask) {
        assertTaskAllPropertiesEquals(expectedTask, getPersistedTask(expectedTask));
    }

    protected void assertPersistedTaskToMatchUpdatableProperties(Task expectedTask) {
        assertTaskAllUpdatablePropertiesEquals(expectedTask, getPersistedTask(expectedTask));
    }
}
