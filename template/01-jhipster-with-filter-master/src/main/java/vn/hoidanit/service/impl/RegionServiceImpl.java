package vn.hoidanit.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.hoidanit.domain.Region;
import vn.hoidanit.repository.RegionRepository;
import vn.hoidanit.service.RegionService;

/**
 * Service Implementation for managing {@link vn.hoidanit.domain.Region}.
 */
@Service
@Transactional
public class RegionServiceImpl implements RegionService {

    private static final Logger LOG = LoggerFactory.getLogger(RegionServiceImpl.class);

    private final RegionRepository regionRepository;

    public RegionServiceImpl(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    @Override
    public Region save(Region region) {
        LOG.debug("Request to save Region : {}", region);
        return regionRepository.save(region);
    }

    @Override
    public Region update(Region region) {
        LOG.debug("Request to update Region : {}", region);
        return regionRepository.save(region);
    }

    @Override
    public Optional<Region> partialUpdate(Region region) {
        LOG.debug("Request to partially update Region : {}", region);

        return regionRepository
            .findById(region.getId())
            .map(existingRegion -> {
                if (region.getRegionName() != null) {
                    existingRegion.setRegionName(region.getRegionName());
                }

                return existingRegion;
            })
            .map(regionRepository::save);
    }

    /**
     *  Get all the regions where Country is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Region> findAllWhereCountryIsNull() {
        LOG.debug("Request to get all regions where Country is null");
        return StreamSupport.stream(regionRepository.findAll().spliterator(), false).filter(region -> region.getCountry() == null).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Region> findOne(Long id) {
        LOG.debug("Request to get Region : {}", id);
        return regionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Region : {}", id);
        regionRepository.deleteById(id);
    }
}
