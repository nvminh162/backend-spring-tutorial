package vn.hoidanit.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import vn.hoidanit.domain.Region;
import vn.hoidanit.repository.RegionRepository;
import vn.hoidanit.service.RegionQueryService;
import vn.hoidanit.service.RegionService;
import vn.hoidanit.service.criteria.RegionCriteria;
import vn.hoidanit.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link vn.hoidanit.domain.Region}.
 */
@RestController
@RequestMapping("/api/regions")
public class RegionResource {

    private static final Logger LOG = LoggerFactory.getLogger(RegionResource.class);

    private static final String ENTITY_NAME = "region";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RegionService regionService;

    private final RegionRepository regionRepository;

    private final RegionQueryService regionQueryService;

    public RegionResource(RegionService regionService, RegionRepository regionRepository, RegionQueryService regionQueryService) {
        this.regionService = regionService;
        this.regionRepository = regionRepository;
        this.regionQueryService = regionQueryService;
    }

    /**
     * {@code POST  /regions} : Create a new region.
     *
     * @param region the region to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new region, or with status {@code 400 (Bad Request)} if the region has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Region> createRegion(@RequestBody Region region) throws URISyntaxException {
        LOG.debug("REST request to save Region : {}", region);
        if (region.getId() != null) {
            throw new BadRequestAlertException("A new region cannot already have an ID", ENTITY_NAME, "idexists");
        }
        region = regionService.save(region);
        return ResponseEntity.created(new URI("/api/regions/" + region.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, region.getId().toString()))
            .body(region);
    }

    /**
     * {@code PUT  /regions/:id} : Updates an existing region.
     *
     * @param id the id of the region to save.
     * @param region the region to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated region,
     * or with status {@code 400 (Bad Request)} if the region is not valid,
     * or with status {@code 500 (Internal Server Error)} if the region couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Region> updateRegion(@PathVariable(value = "id", required = false) final Long id, @RequestBody Region region)
        throws URISyntaxException {
        LOG.debug("REST request to update Region : {}, {}", id, region);
        if (region.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, region.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!regionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        region = regionService.update(region);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, region.getId().toString()))
            .body(region);
    }

    /**
     * {@code PATCH  /regions/:id} : Partial updates given fields of an existing region, field will ignore if it is null
     *
     * @param id the id of the region to save.
     * @param region the region to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated region,
     * or with status {@code 400 (Bad Request)} if the region is not valid,
     * or with status {@code 404 (Not Found)} if the region is not found,
     * or with status {@code 500 (Internal Server Error)} if the region couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Region> partialUpdateRegion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Region region
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Region partially : {}, {}", id, region);
        if (region.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, region.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!regionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Region> result = regionService.partialUpdate(region);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, region.getId().toString())
        );
    }

    /**
     * {@code GET  /regions} : get all the regions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of regions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Region>> getAllRegions(RegionCriteria criteria) {
        LOG.debug("REST request to get Regions by criteria: {}", criteria);

        List<Region> entityList = regionQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /regions/count} : count all the regions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countRegions(RegionCriteria criteria) {
        LOG.debug("REST request to count Regions by criteria: {}", criteria);
        return ResponseEntity.ok().body(regionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /regions/:id} : get the "id" region.
     *
     * @param id the id of the region to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the region, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Region> getRegion(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Region : {}", id);
        Optional<Region> region = regionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(region);
    }

    /**
     * {@code DELETE  /regions/:id} : delete the "id" region.
     *
     * @param id the id of the region to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegion(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Region : {}", id);
        regionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
