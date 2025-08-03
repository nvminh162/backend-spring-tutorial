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
import vn.hoidanit.domain.Country;
import vn.hoidanit.repository.CountryRepository;
import vn.hoidanit.service.CountryQueryService;
import vn.hoidanit.service.CountryService;
import vn.hoidanit.service.criteria.CountryCriteria;
import vn.hoidanit.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link vn.hoidanit.domain.Country}.
 */
@RestController
@RequestMapping("/api/countries")
public class CountryResource {

    private static final Logger LOG = LoggerFactory.getLogger(CountryResource.class);

    private static final String ENTITY_NAME = "country";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CountryService countryService;

    private final CountryRepository countryRepository;

    private final CountryQueryService countryQueryService;

    public CountryResource(CountryService countryService, CountryRepository countryRepository, CountryQueryService countryQueryService) {
        this.countryService = countryService;
        this.countryRepository = countryRepository;
        this.countryQueryService = countryQueryService;
    }

    /**
     * {@code POST  /countries} : Create a new country.
     *
     * @param country the country to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new country, or with status {@code 400 (Bad Request)} if the country has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Country> createCountry(@RequestBody Country country) throws URISyntaxException {
        LOG.debug("REST request to save Country : {}", country);
        if (country.getId() != null) {
            throw new BadRequestAlertException("A new country cannot already have an ID", ENTITY_NAME, "idexists");
        }
        country = countryService.save(country);
        return ResponseEntity.created(new URI("/api/countries/" + country.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, country.getId().toString()))
            .body(country);
    }

    /**
     * {@code PUT  /countries/:id} : Updates an existing country.
     *
     * @param id the id of the country to save.
     * @param country the country to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated country,
     * or with status {@code 400 (Bad Request)} if the country is not valid,
     * or with status {@code 500 (Internal Server Error)} if the country couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Country> updateCountry(@PathVariable(value = "id", required = false) final Long id, @RequestBody Country country)
        throws URISyntaxException {
        LOG.debug("REST request to update Country : {}, {}", id, country);
        if (country.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, country.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!countryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        country = countryService.update(country);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, country.getId().toString()))
            .body(country);
    }

    /**
     * {@code PATCH  /countries/:id} : Partial updates given fields of an existing country, field will ignore if it is null
     *
     * @param id the id of the country to save.
     * @param country the country to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated country,
     * or with status {@code 400 (Bad Request)} if the country is not valid,
     * or with status {@code 404 (Not Found)} if the country is not found,
     * or with status {@code 500 (Internal Server Error)} if the country couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Country> partialUpdateCountry(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Country country
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Country partially : {}, {}", id, country);
        if (country.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, country.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!countryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Country> result = countryService.partialUpdate(country);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, country.getId().toString())
        );
    }

    /**
     * {@code GET  /countries} : get all the countries.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of countries in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Country>> getAllCountries(CountryCriteria criteria) {
        LOG.debug("REST request to get Countries by criteria: {}", criteria);

        List<Country> entityList = countryQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /countries/count} : count all the countries.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCountries(CountryCriteria criteria) {
        LOG.debug("REST request to count Countries by criteria: {}", criteria);
        return ResponseEntity.ok().body(countryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /countries/:id} : get the "id" country.
     *
     * @param id the id of the country to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the country, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Country> getCountry(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Country : {}", id);
        Optional<Country> country = countryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(country);
    }

    /**
     * {@code DELETE  /countries/:id} : delete the "id" country.
     *
     * @param id the id of the country to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCountry(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Country : {}", id);
        countryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
