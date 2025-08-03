package vn.hoidanit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static vn.hoidanit.domain.RegionAsserts.*;
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
import vn.hoidanit.domain.Region;
import vn.hoidanit.repository.RegionRepository;

/**
 * Integration tests for the {@link RegionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RegionResourceIT {

    private static final String DEFAULT_REGION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_REGION_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/regions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRegionMockMvc;

    private Region region;

    private Region insertedRegion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Region createEntity() {
        return new Region().regionName(DEFAULT_REGION_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Region createUpdatedEntity() {
        return new Region().regionName(UPDATED_REGION_NAME);
    }

    @BeforeEach
    public void initTest() {
        region = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedRegion != null) {
            regionRepository.delete(insertedRegion);
            insertedRegion = null;
        }
    }

    @Test
    @Transactional
    void createRegion() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Region
        var returnedRegion = om.readValue(
            restRegionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(region)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Region.class
        );

        // Validate the Region in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertRegionUpdatableFieldsEquals(returnedRegion, getPersistedRegion(returnedRegion));

        insertedRegion = returnedRegion;
    }

    @Test
    @Transactional
    void createRegionWithExistingId() throws Exception {
        // Create the Region with an existing ID
        region.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRegionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(region)))
            .andExpect(status().isBadRequest());

        // Validate the Region in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRegions() throws Exception {
        // Initialize the database
        insertedRegion = regionRepository.saveAndFlush(region);

        // Get all the regionList
        restRegionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(region.getId().intValue())))
            .andExpect(jsonPath("$.[*].regionName").value(hasItem(DEFAULT_REGION_NAME)));
    }

    @Test
    @Transactional
    void getRegion() throws Exception {
        // Initialize the database
        insertedRegion = regionRepository.saveAndFlush(region);

        // Get the region
        restRegionMockMvc
            .perform(get(ENTITY_API_URL_ID, region.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(region.getId().intValue()))
            .andExpect(jsonPath("$.regionName").value(DEFAULT_REGION_NAME));
    }

    @Test
    @Transactional
    void getRegionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedRegion = regionRepository.saveAndFlush(region);

        Long id = region.getId();

        defaultRegionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultRegionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultRegionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRegionsByRegionNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRegion = regionRepository.saveAndFlush(region);

        // Get all the regionList where regionName equals to
        defaultRegionFiltering("regionName.equals=" + DEFAULT_REGION_NAME, "regionName.equals=" + UPDATED_REGION_NAME);
    }

    @Test
    @Transactional
    void getAllRegionsByRegionNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRegion = regionRepository.saveAndFlush(region);

        // Get all the regionList where regionName in
        defaultRegionFiltering("regionName.in=" + DEFAULT_REGION_NAME + "," + UPDATED_REGION_NAME, "regionName.in=" + UPDATED_REGION_NAME);
    }

    @Test
    @Transactional
    void getAllRegionsByRegionNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRegion = regionRepository.saveAndFlush(region);

        // Get all the regionList where regionName is not null
        defaultRegionFiltering("regionName.specified=true", "regionName.specified=false");
    }

    @Test
    @Transactional
    void getAllRegionsByRegionNameContainsSomething() throws Exception {
        // Initialize the database
        insertedRegion = regionRepository.saveAndFlush(region);

        // Get all the regionList where regionName contains
        defaultRegionFiltering("regionName.contains=" + DEFAULT_REGION_NAME, "regionName.contains=" + UPDATED_REGION_NAME);
    }

    @Test
    @Transactional
    void getAllRegionsByRegionNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedRegion = regionRepository.saveAndFlush(region);

        // Get all the regionList where regionName does not contain
        defaultRegionFiltering("regionName.doesNotContain=" + UPDATED_REGION_NAME, "regionName.doesNotContain=" + DEFAULT_REGION_NAME);
    }

    private void defaultRegionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultRegionShouldBeFound(shouldBeFound);
        defaultRegionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRegionShouldBeFound(String filter) throws Exception {
        restRegionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(region.getId().intValue())))
            .andExpect(jsonPath("$.[*].regionName").value(hasItem(DEFAULT_REGION_NAME)));

        // Check, that the count call also returns 1
        restRegionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRegionShouldNotBeFound(String filter) throws Exception {
        restRegionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRegionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRegion() throws Exception {
        // Get the region
        restRegionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRegion() throws Exception {
        // Initialize the database
        insertedRegion = regionRepository.saveAndFlush(region);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the region
        Region updatedRegion = regionRepository.findById(region.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRegion are not directly saved in db
        em.detach(updatedRegion);
        updatedRegion.regionName(UPDATED_REGION_NAME);

        restRegionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRegion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedRegion))
            )
            .andExpect(status().isOk());

        // Validate the Region in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRegionToMatchAllProperties(updatedRegion);
    }

    @Test
    @Transactional
    void putNonExistingRegion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        region.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRegionMockMvc
            .perform(put(ENTITY_API_URL_ID, region.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(region)))
            .andExpect(status().isBadRequest());

        // Validate the Region in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRegion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        region.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(region))
            )
            .andExpect(status().isBadRequest());

        // Validate the Region in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRegion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        region.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(region)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Region in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRegionWithPatch() throws Exception {
        // Initialize the database
        insertedRegion = regionRepository.saveAndFlush(region);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the region using partial update
        Region partialUpdatedRegion = new Region();
        partialUpdatedRegion.setId(region.getId());

        restRegionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRegion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRegion))
            )
            .andExpect(status().isOk());

        // Validate the Region in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRegionUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedRegion, region), getPersistedRegion(region));
    }

    @Test
    @Transactional
    void fullUpdateRegionWithPatch() throws Exception {
        // Initialize the database
        insertedRegion = regionRepository.saveAndFlush(region);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the region using partial update
        Region partialUpdatedRegion = new Region();
        partialUpdatedRegion.setId(region.getId());

        partialUpdatedRegion.regionName(UPDATED_REGION_NAME);

        restRegionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRegion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRegion))
            )
            .andExpect(status().isOk());

        // Validate the Region in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRegionUpdatableFieldsEquals(partialUpdatedRegion, getPersistedRegion(partialUpdatedRegion));
    }

    @Test
    @Transactional
    void patchNonExistingRegion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        region.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRegionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, region.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(region))
            )
            .andExpect(status().isBadRequest());

        // Validate the Region in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRegion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        region.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(region))
            )
            .andExpect(status().isBadRequest());

        // Validate the Region in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRegion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        region.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(region)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Region in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRegion() throws Exception {
        // Initialize the database
        insertedRegion = regionRepository.saveAndFlush(region);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the region
        restRegionMockMvc
            .perform(delete(ENTITY_API_URL_ID, region.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return regionRepository.count();
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

    protected Region getPersistedRegion(Region region) {
        return regionRepository.findById(region.getId()).orElseThrow();
    }

    protected void assertPersistedRegionToMatchAllProperties(Region expectedRegion) {
        assertRegionAllPropertiesEquals(expectedRegion, getPersistedRegion(expectedRegion));
    }

    protected void assertPersistedRegionToMatchUpdatableProperties(Region expectedRegion) {
        assertRegionAllUpdatablePropertiesEquals(expectedRegion, getPersistedRegion(expectedRegion));
    }
}
