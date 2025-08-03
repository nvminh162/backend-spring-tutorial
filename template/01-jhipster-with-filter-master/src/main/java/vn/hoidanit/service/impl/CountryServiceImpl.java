package vn.hoidanit.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.hoidanit.domain.Country;
import vn.hoidanit.repository.CountryRepository;
import vn.hoidanit.service.CountryService;

/**
 * Service Implementation for managing {@link vn.hoidanit.domain.Country}.
 */
@Service
@Transactional
public class CountryServiceImpl implements CountryService {

    private static final Logger LOG = LoggerFactory.getLogger(CountryServiceImpl.class);

    private final CountryRepository countryRepository;

    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public Country save(Country country) {
        LOG.debug("Request to save Country : {}", country);
        return countryRepository.save(country);
    }

    @Override
    public Country update(Country country) {
        LOG.debug("Request to update Country : {}", country);
        return countryRepository.save(country);
    }

    @Override
    public Optional<Country> partialUpdate(Country country) {
        LOG.debug("Request to partially update Country : {}", country);

        return countryRepository
            .findById(country.getId())
            .map(existingCountry -> {
                if (country.getCountryName() != null) {
                    existingCountry.setCountryName(country.getCountryName());
                }

                return existingCountry;
            })
            .map(countryRepository::save);
    }

    /**
     *  Get all the countries where Location is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Country> findAllWhereLocationIsNull() {
        LOG.debug("Request to get all countries where Location is null");
        return StreamSupport.stream(countryRepository.findAll().spliterator(), false)
            .filter(country -> country.getLocation() == null)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Country> findOne(Long id) {
        LOG.debug("Request to get Country : {}", id);
        return countryRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Country : {}", id);
        countryRepository.deleteById(id);
    }
}
