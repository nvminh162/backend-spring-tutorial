package vn.hoidanit.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link vn.hoidanit.domain.Location} entity. This class is used
 * in {@link vn.hoidanit.web.rest.LocationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /locations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LocationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter streetAddress;

    private StringFilter postalCode;

    private StringFilter city;

    private StringFilter stateProvince;

    private LongFilter countryId;

    private LongFilter departmentId;

    private Boolean distinct;

    public LocationCriteria() {}

    public LocationCriteria(LocationCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.streetAddress = other.optionalStreetAddress().map(StringFilter::copy).orElse(null);
        this.postalCode = other.optionalPostalCode().map(StringFilter::copy).orElse(null);
        this.city = other.optionalCity().map(StringFilter::copy).orElse(null);
        this.stateProvince = other.optionalStateProvince().map(StringFilter::copy).orElse(null);
        this.countryId = other.optionalCountryId().map(LongFilter::copy).orElse(null);
        this.departmentId = other.optionalDepartmentId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public LocationCriteria copy() {
        return new LocationCriteria(this);
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

    public StringFilter getStreetAddress() {
        return streetAddress;
    }

    public Optional<StringFilter> optionalStreetAddress() {
        return Optional.ofNullable(streetAddress);
    }

    public StringFilter streetAddress() {
        if (streetAddress == null) {
            setStreetAddress(new StringFilter());
        }
        return streetAddress;
    }

    public void setStreetAddress(StringFilter streetAddress) {
        this.streetAddress = streetAddress;
    }

    public StringFilter getPostalCode() {
        return postalCode;
    }

    public Optional<StringFilter> optionalPostalCode() {
        return Optional.ofNullable(postalCode);
    }

    public StringFilter postalCode() {
        if (postalCode == null) {
            setPostalCode(new StringFilter());
        }
        return postalCode;
    }

    public void setPostalCode(StringFilter postalCode) {
        this.postalCode = postalCode;
    }

    public StringFilter getCity() {
        return city;
    }

    public Optional<StringFilter> optionalCity() {
        return Optional.ofNullable(city);
    }

    public StringFilter city() {
        if (city == null) {
            setCity(new StringFilter());
        }
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public StringFilter getStateProvince() {
        return stateProvince;
    }

    public Optional<StringFilter> optionalStateProvince() {
        return Optional.ofNullable(stateProvince);
    }

    public StringFilter stateProvince() {
        if (stateProvince == null) {
            setStateProvince(new StringFilter());
        }
        return stateProvince;
    }

    public void setStateProvince(StringFilter stateProvince) {
        this.stateProvince = stateProvince;
    }

    public LongFilter getCountryId() {
        return countryId;
    }

    public Optional<LongFilter> optionalCountryId() {
        return Optional.ofNullable(countryId);
    }

    public LongFilter countryId() {
        if (countryId == null) {
            setCountryId(new LongFilter());
        }
        return countryId;
    }

    public void setCountryId(LongFilter countryId) {
        this.countryId = countryId;
    }

    public LongFilter getDepartmentId() {
        return departmentId;
    }

    public Optional<LongFilter> optionalDepartmentId() {
        return Optional.ofNullable(departmentId);
    }

    public LongFilter departmentId() {
        if (departmentId == null) {
            setDepartmentId(new LongFilter());
        }
        return departmentId;
    }

    public void setDepartmentId(LongFilter departmentId) {
        this.departmentId = departmentId;
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
        final LocationCriteria that = (LocationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(streetAddress, that.streetAddress) &&
            Objects.equals(postalCode, that.postalCode) &&
            Objects.equals(city, that.city) &&
            Objects.equals(stateProvince, that.stateProvince) &&
            Objects.equals(countryId, that.countryId) &&
            Objects.equals(departmentId, that.departmentId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, streetAddress, postalCode, city, stateProvince, countryId, departmentId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LocationCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalStreetAddress().map(f -> "streetAddress=" + f + ", ").orElse("") +
            optionalPostalCode().map(f -> "postalCode=" + f + ", ").orElse("") +
            optionalCity().map(f -> "city=" + f + ", ").orElse("") +
            optionalStateProvince().map(f -> "stateProvince=" + f + ", ").orElse("") +
            optionalCountryId().map(f -> "countryId=" + f + ", ").orElse("") +
            optionalDepartmentId().map(f -> "departmentId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
