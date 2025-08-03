package vn.hoidanit.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link vn.hoidanit.domain.Country} entity. This class is used
 * in {@link vn.hoidanit.web.rest.CountryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /countries?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CountryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter countryName;

    private LongFilter regionId;

    private LongFilter locationId;

    private Boolean distinct;

    public CountryCriteria() {}

    public CountryCriteria(CountryCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.countryName = other.optionalCountryName().map(StringFilter::copy).orElse(null);
        this.regionId = other.optionalRegionId().map(LongFilter::copy).orElse(null);
        this.locationId = other.optionalLocationId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CountryCriteria copy() {
        return new CountryCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCountryName() {
        return countryName;
    }

    public Optional<StringFilter> optionalCountryName() {
        return Optional.ofNullable(countryName);
    }

    public StringFilter countryName() {
        if (countryName == null) {
            setCountryName(new StringFilter());
        }
        return countryName;
    }

    public void setCountryName(StringFilter countryName) {
        this.countryName = countryName;
    }

    public LongFilter getRegionId() {
        return regionId;
    }

    public Optional<LongFilter> optionalRegionId() {
        return Optional.ofNullable(regionId);
    }

    public LongFilter regionId() {
        if (regionId == null) {
            setRegionId(new LongFilter());
        }
        return regionId;
    }

    public void setRegionId(LongFilter regionId) {
        this.regionId = regionId;
    }

    public LongFilter getLocationId() {
        return locationId;
    }

    public Optional<LongFilter> optionalLocationId() {
        return Optional.ofNullable(locationId);
    }

    public LongFilter locationId() {
        if (locationId == null) {
            setLocationId(new LongFilter());
        }
        return locationId;
    }

    public void setLocationId(LongFilter locationId) {
        this.locationId = locationId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CountryCriteria that = (CountryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(countryName, that.countryName) &&
            Objects.equals(regionId, that.regionId) &&
            Objects.equals(locationId, that.locationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, countryName, regionId, locationId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CountryCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCountryName().map(f -> "countryName=" + f + ", ").orElse("") +
            optionalRegionId().map(f -> "regionId=" + f + ", ").orElse("") +
            optionalLocationId().map(f -> "locationId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
