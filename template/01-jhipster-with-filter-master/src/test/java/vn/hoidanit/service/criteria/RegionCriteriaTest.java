package vn.hoidanit.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class RegionCriteriaTest {

    @Test
    void newRegionCriteriaHasAllFiltersNullTest() {
        var regionCriteria = new RegionCriteria();
        assertThat(regionCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void regionCriteriaFluentMethodsCreatesFiltersTest() {
        var regionCriteria = new RegionCriteria();

        setAllFilters(regionCriteria);

        assertThat(regionCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void regionCriteriaCopyCreatesNullFilterTest() {
        var regionCriteria = new RegionCriteria();
        var copy = regionCriteria.copy();

        assertThat(regionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(regionCriteria)
        );
    }

    @Test
    void regionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var regionCriteria = new RegionCriteria();
        setAllFilters(regionCriteria);

        var copy = regionCriteria.copy();

        assertThat(regionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(regionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var regionCriteria = new RegionCriteria();

        assertThat(regionCriteria).hasToString("RegionCriteria{}");
    }

    private static void setAllFilters(RegionCriteria regionCriteria) {
        regionCriteria.id();
        regionCriteria.regionName();
        regionCriteria.countryId();
        regionCriteria.distinct();
    }

    private static Condition<RegionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getRegionName()) &&
                condition.apply(criteria.getCountryId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<RegionCriteria> copyFiltersAre(RegionCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getRegionName(), copy.getRegionName()) &&
                condition.apply(criteria.getCountryId(), copy.getCountryId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
