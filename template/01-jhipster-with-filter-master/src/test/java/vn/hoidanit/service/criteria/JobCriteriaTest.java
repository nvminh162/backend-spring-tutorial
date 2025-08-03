package vn.hoidanit.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class JobCriteriaTest {

    @Test
    void newJobCriteriaHasAllFiltersNullTest() {
        var jobCriteria = new JobCriteria();
        assertThat(jobCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void jobCriteriaFluentMethodsCreatesFiltersTest() {
        var jobCriteria = new JobCriteria();

        setAllFilters(jobCriteria);

        assertThat(jobCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void jobCriteriaCopyCreatesNullFilterTest() {
        var jobCriteria = new JobCriteria();
        var copy = jobCriteria.copy();

        assertThat(jobCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(jobCriteria)
        );
    }

    @Test
    void jobCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var jobCriteria = new JobCriteria();
        setAllFilters(jobCriteria);

        var copy = jobCriteria.copy();

        assertThat(jobCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(jobCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var jobCriteria = new JobCriteria();

        assertThat(jobCriteria).hasToString("JobCriteria{}");
    }

    private static void setAllFilters(JobCriteria jobCriteria) {
        jobCriteria.id();
        jobCriteria.jobTitle();
        jobCriteria.minSalary();
        jobCriteria.maxSalary();
        jobCriteria.taskId();
        jobCriteria.employeeId();
        jobCriteria.jobHistoryId();
        jobCriteria.distinct();
    }

    private static Condition<JobCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getJobTitle()) &&
                condition.apply(criteria.getMinSalary()) &&
                condition.apply(criteria.getMaxSalary()) &&
                condition.apply(criteria.getTaskId()) &&
                condition.apply(criteria.getEmployeeId()) &&
                condition.apply(criteria.getJobHistoryId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<JobCriteria> copyFiltersAre(JobCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getJobTitle(), copy.getJobTitle()) &&
                condition.apply(criteria.getMinSalary(), copy.getMinSalary()) &&
                condition.apply(criteria.getMaxSalary(), copy.getMaxSalary()) &&
                condition.apply(criteria.getTaskId(), copy.getTaskId()) &&
                condition.apply(criteria.getEmployeeId(), copy.getEmployeeId()) &&
                condition.apply(criteria.getJobHistoryId(), copy.getJobHistoryId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
