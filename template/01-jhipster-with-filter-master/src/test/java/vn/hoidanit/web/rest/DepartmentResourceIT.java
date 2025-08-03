package vn.hoidanit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static vn.hoidanit.domain.DepartmentAsserts.*;
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
import vn.hoidanit.domain.Department;
import vn.hoidanit.domain.Location;
import vn.hoidanit.repository.DepartmentRepository;

/**
 * Integration tests for the {@link DepartmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DepartmentResourceIT {

    private static final String DEFAULT_DEPARTMENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DEPARTMENT_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/departments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDepartmentMockMvc;

    private Department department;

    private Department insertedDepartment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Department createEntity() {
        return new Department().departmentName(DEFAULT_DEPARTMENT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Department createUpdatedEntity() {
        return new Department().departmentName(UPDATED_DEPARTMENT_NAME);
    }

    @BeforeEach
    public void initTest() {
        department = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedDepartment != null) {
            departmentRepository.delete(insertedDepartment);
            insertedDepartment = null;
        }
    }

    @Test
    @Transactional
    void createDepartment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Department
        var returnedDepartment = om.readValue(
            restDepartmentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(department)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Department.class
        );

        // Validate the Department in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertDepartmentUpdatableFieldsEquals(returnedDepartment, getPersistedDepartment(returnedDepartment));

        insertedDepartment = returnedDepartment;
    }

    @Test
    @Transactional
    void createDepartmentWithExistingId() throws Exception {
        // Create the Department with an existing ID
        department.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepartmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(department)))
            .andExpect(status().isBadRequest());

        // Validate the Department in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDepartmentNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        department.setDepartmentName(null);

        // Create the Department, which fails.

        restDepartmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(department)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDepartments() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndFlush(department);

        // Get all the departmentList
        restDepartmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(department.getId().intValue())))
            .andExpect(jsonPath("$.[*].departmentName").value(hasItem(DEFAULT_DEPARTMENT_NAME)));
    }

    @Test
    @Transactional
    void getDepartment() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndFlush(department);

        // Get the department
        restDepartmentMockMvc
            .perform(get(ENTITY_API_URL_ID, department.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(department.getId().intValue()))
            .andExpect(jsonPath("$.departmentName").value(DEFAULT_DEPARTMENT_NAME));
    }

    @Test
    @Transactional
    void getDepartmentsByIdFiltering() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndFlush(department);

        Long id = department.getId();

        defaultDepartmentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDepartmentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDepartmentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDepartmentsByDepartmentNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndFlush(department);

        // Get all the departmentList where departmentName equals to
        defaultDepartmentFiltering("departmentName.equals=" + DEFAULT_DEPARTMENT_NAME, "departmentName.equals=" + UPDATED_DEPARTMENT_NAME);
    }

    @Test
    @Transactional
    void getAllDepartmentsByDepartmentNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndFlush(department);

        // Get all the departmentList where departmentName in
        defaultDepartmentFiltering(
            "departmentName.in=" + DEFAULT_DEPARTMENT_NAME + "," + UPDATED_DEPARTMENT_NAME,
            "departmentName.in=" + UPDATED_DEPARTMENT_NAME
        );
    }

    @Test
    @Transactional
    void getAllDepartmentsByDepartmentNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndFlush(department);

        // Get all the departmentList where departmentName is not null
        defaultDepartmentFiltering("departmentName.specified=true", "departmentName.specified=false");
    }

    @Test
    @Transactional
    void getAllDepartmentsByDepartmentNameContainsSomething() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndFlush(department);

        // Get all the departmentList where departmentName contains
        defaultDepartmentFiltering(
            "departmentName.contains=" + DEFAULT_DEPARTMENT_NAME,
            "departmentName.contains=" + UPDATED_DEPARTMENT_NAME
        );
    }

    @Test
    @Transactional
    void getAllDepartmentsByDepartmentNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndFlush(department);

        // Get all the departmentList where departmentName does not contain
        defaultDepartmentFiltering(
            "departmentName.doesNotContain=" + UPDATED_DEPARTMENT_NAME,
            "departmentName.doesNotContain=" + DEFAULT_DEPARTMENT_NAME
        );
    }

    @Test
    @Transactional
    void getAllDepartmentsByLocationIsEqualToSomething() throws Exception {
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            departmentRepository.saveAndFlush(department);
            location = LocationResourceIT.createEntity();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        em.persist(location);
        em.flush();
        department.setLocation(location);
        departmentRepository.saveAndFlush(department);
        Long locationId = location.getId();
        // Get all the departmentList where location equals to locationId
        defaultDepartmentShouldBeFound("locationId.equals=" + locationId);

        // Get all the departmentList where location equals to (locationId + 1)
        defaultDepartmentShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }

    private void defaultDepartmentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDepartmentShouldBeFound(shouldBeFound);
        defaultDepartmentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDepartmentShouldBeFound(String filter) throws Exception {
        restDepartmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(department.getId().intValue())))
            .andExpect(jsonPath("$.[*].departmentName").value(hasItem(DEFAULT_DEPARTMENT_NAME)));

        // Check, that the count call also returns 1
        restDepartmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDepartmentShouldNotBeFound(String filter) throws Exception {
        restDepartmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDepartmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDepartment() throws Exception {
        // Get the department
        restDepartmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDepartment() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndFlush(department);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the department
        Department updatedDepartment = departmentRepository.findById(department.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDepartment are not directly saved in db
        em.detach(updatedDepartment);
        updatedDepartment.departmentName(UPDATED_DEPARTMENT_NAME);

        restDepartmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDepartment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedDepartment))
            )
            .andExpect(status().isOk());

        // Validate the Department in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDepartmentToMatchAllProperties(updatedDepartment);
    }

    @Test
    @Transactional
    void putNonExistingDepartment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        department.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepartmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, department.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(department))
            )
            .andExpect(status().isBadRequest());

        // Validate the Department in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDepartment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        department.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(department))
            )
            .andExpect(status().isBadRequest());

        // Validate the Department in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDepartment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        department.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(department)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Department in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDepartmentWithPatch() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndFlush(department);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the department using partial update
        Department partialUpdatedDepartment = new Department();
        partialUpdatedDepartment.setId(department.getId());

        restDepartmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepartment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDepartment))
            )
            .andExpect(status().isOk());

        // Validate the Department in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDepartmentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDepartment, department),
            getPersistedDepartment(department)
        );
    }

    @Test
    @Transactional
    void fullUpdateDepartmentWithPatch() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndFlush(department);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the department using partial update
        Department partialUpdatedDepartment = new Department();
        partialUpdatedDepartment.setId(department.getId());

        partialUpdatedDepartment.departmentName(UPDATED_DEPARTMENT_NAME);

        restDepartmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepartment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDepartment))
            )
            .andExpect(status().isOk());

        // Validate the Department in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDepartmentUpdatableFieldsEquals(partialUpdatedDepartment, getPersistedDepartment(partialUpdatedDepartment));
    }

    @Test
    @Transactional
    void patchNonExistingDepartment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        department.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepartmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, department.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(department))
            )
            .andExpect(status().isBadRequest());

        // Validate the Department in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDepartment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        department.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(department))
            )
            .andExpect(status().isBadRequest());

        // Validate the Department in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDepartment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        department.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartmentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(department)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Department in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDepartment() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndFlush(department);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the department
        restDepartmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, department.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return departmentRepository.count();
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

    protected Department getPersistedDepartment(Department department) {
        return departmentRepository.findById(department.getId()).orElseThrow();
    }

    protected void assertPersistedDepartmentToMatchAllProperties(Department expectedDepartment) {
        assertDepartmentAllPropertiesEquals(expectedDepartment, getPersistedDepartment(expectedDepartment));
    }

    protected void assertPersistedDepartmentToMatchUpdatableProperties(Department expectedDepartment) {
        assertDepartmentAllUpdatablePropertiesEquals(expectedDepartment, getPersistedDepartment(expectedDepartment));
    }
}
