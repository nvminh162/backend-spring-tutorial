package vn.hoidanit.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class JobHistoryCriteriaTest {

    @Test
    void newJobHistoryCriteriaHasAllFiltersNullTest() {
        var jobHistoryCriteria = new JobHistoryCriteria();
        assertThat(jobHistoryCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void jobHistoryCriteriaFluentMethodsCreatesFiltersTest() {
        var jobHistoryCriteria = new JobHistoryCriteria();

        setAllFilters(jobHistoryCriteria);

        assertThat(jobHistoryCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void jobHistoryCriteriaCopyCreatesNullFilterTest() {
        var jobHistoryCriteria = new JobHistoryCriteria();
        var copy = jobHistoryCriteria.copy();

        assertThat(jobHistoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(jobHistoryCriteria)
        );
    }

    @Test
    void jobHistoryCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var jobHistoryCriteria = new JobHistoryCriteria();
        setAllFilters(jobHistoryCriteria);

        var copy = jobHistoryCriteria.copy();

        assertThat(jobHistoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(jobHistoryCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var jobHistoryCriteria = new JobHistoryCriteria();

        assertThat(jobHistoryCriteria).hasToString("JobHistoryCriteria{}");
    }

    private static void setAllFilters(JobHistoryCriteria jobHistoryCriteria) {
        jobHistoryCriteria.id();
        jobHistoryCriteria.startDate();
        jobHistoryCriteria.endDate();
        jobHistoryCriteria.language();
        jobHistoryCriteria.jobId();
        jobHistoryCriteria.departmentId();
        jobHistoryCriteria.employeeId();
        jobHistoryCriteria.distinct();
    }

    private static Condition<JobHistoryCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getStartDate()) &&
                condition.apply(criteria.getEndDate()) &&
                condition.apply(criteria.getLanguage()) &&
                condition.apply(criteria.getJobId()) &&
                condition.apply(criteria.getDepartmentId()) &&
                condition.apply(criteria.getEmployeeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<JobHistoryCriteria> copyFiltersAre(JobHistoryCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getStartDate(), copy.getStartDate()) &&
                condition.apply(criteria.getEndDate(), copy.getEndDate()) &&
                condition.apply(criteria.getLanguage(), copy.getLanguage()) &&
                condition.apply(criteria.getJobId(), copy.getJobId()) &&
                condition.apply(criteria.getDepartmentId(), copy.getDepartmentId()) &&
                condition.apply(criteria.getEmployeeId(), copy.getEmployeeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
