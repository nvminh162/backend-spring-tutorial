package vn.hoidanit.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.hoidanit.domain.Location;
import vn.hoidanit.repository.LocationRepository;
import vn.hoidanit.service.LocationService;

/**
 * Service Implementation for managing {@link vn.hoidanit.domain.Location}.
 */
@Service
@Transactional
public class LocationServiceImpl implements LocationService {

    private static final Logger LOG = LoggerFactory.getLogger(LocationServiceImpl.class);

    private final LocationRepository locationRepository;

    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public Location save(Location location) {
        LOG.debug("Request to save Location : {}", location);
        return locationRepository.save(location);
    }

    @Override
    public Location update(Location location) {
        LOG.debug("Request to update Location : {}", location);
        return locationRepository.save(location);
    }

    @Override
    public Optional<Location> partialUpdate(Location location) {
        LOG.debug("Request to partially update Location : {}", location);

        return locationRepository
            .findById(location.getId())
            .map(existingLocation -> {
                if (location.getStreetAddress() != null) {
                    existingLocation.setStreetAddress(location.getStreetAddress());
                }
                if (location.getPostalCode() != null) {
                    existingLocation.setPostalCode(location.getPostalCode());
                }
                if (location.getCity() != null) {
                    existingLocation.setCity(location.getCity());
                }
                if (location.getStateProvince() != null) {
                    existingLocation.setStateProvince(location.getStateProvince());
                }

                return existingLocation;
            })
            .map(locationRepository::save);
    }

    /**
     *  Get all the locations where Department is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Location> findAllWhereDepartmentIsNull() {
        LOG.debug("Request to get all locations where Department is null");
        return StreamSupport.stream(locationRepository.findAll().spliterator(), false)
            .filter(location -> location.getDepartment() == null)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Location> findOne(Long id) {
        LOG.debug("Request to get Location : {}", id);
        return locationRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Location : {}", id);
        locationRepository.deleteById(id);
    }
}
