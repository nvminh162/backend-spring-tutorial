package vn.hoidanit.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DepartmentCriteriaTest {

    @Test
    void newDepartmentCriteriaHasAllFiltersNullTest() {
        var departmentCriteria = new DepartmentCriteria();
        assertThat(departmentCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void departmentCriteriaFluentMethodsCreatesFiltersTest() {
        var departmentCriteria = new DepartmentCriteria();

        setAllFilters(departmentCriteria);

        assertThat(departmentCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void departmentCriteriaCopyCreatesNullFilterTest() {
        var departmentCriteria = new DepartmentCriteria();
        var copy = departmentCriteria.copy();

        assertThat(departmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(departmentCriteria)
        );
    }

    @Test
    void departmentCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var departmentCriteria = new DepartmentCriteria();
        setAllFilters(departmentCriteria);

        var copy = departmentCriteria.copy();

        assertThat(departmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(departmentCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var departmentCriteria = new DepartmentCriteria();

        assertThat(departmentCriteria).hasToString("DepartmentCriteria{}");
    }

    private static void setAllFilters(DepartmentCriteria departmentCriteria) {
        departmentCriteria.id();
        departmentCriteria.departmentName();
        departmentCriteria.locationId();
        departmentCriteria.employeeId();
        departmentCriteria.jobHistoryId();
        departmentCriteria.distinct();
    }

    private static Condition<DepartmentCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDepartmentName()) &&
                condition.apply(criteria.getLocationId()) &&
                condition.apply(criteria.getEmployeeId()) &&
                condition.apply(criteria.getJobHistoryId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DepartmentCriteria> copyFiltersAre(DepartmentCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDepartmentName(), copy.getDepartmentName()) &&
                condition.apply(criteria.getLocationId(), copy.getLocationId()) &&
                condition.apply(criteria.getEmployeeId(), copy.getEmployeeId()) &&
                condition.apply(criteria.getJobHistoryId(), copy.getJobHistoryId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
