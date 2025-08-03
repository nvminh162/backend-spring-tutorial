package vn.hoidanit.service;

import java.util.List;
import java.util.Optional;
import vn.hoidanit.domain.Country;

/**
 * Service Interface for managing {@link vn.hoidanit.domain.Country}.
 */
public interface CountryService {
    /**
     * Save a country.
     *
     * @param country the entity to save.
     * @return the persisted entity.
     */
    Country save(Country country);

    /**
     * Updates a country.
     *
     * @param country the entity to update.
     * @return the persisted entity.
     */
    Country update(Country country);

    /**
     * Partially updates a country.
     *
     * @param country the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Country> partialUpdate(Country country);

    /**
     * Get all the Country where Location is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Country> findAllWhereLocationIsNull();

    /**
     * Get the "id" country.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Country> findOne(Long id);

    /**
     * Delete the "id" country.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
