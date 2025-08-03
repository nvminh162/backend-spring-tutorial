package vn.hoidanit.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link vn.hoidanit.domain.Job} entity. This class is used
 * in {@link vn.hoidanit.web.rest.JobResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /jobs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class JobCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter jobTitle;

    private LongFilter minSalary;

    private LongFilter maxSalary;

    private LongFilter taskId;

    private LongFilter employeeId;

    private LongFilter jobHistoryId;

    private Boolean distinct;

    public JobCriteria() {}

    public JobCriteria(JobCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.jobTitle = other.optionalJobTitle().map(StringFilter::copy).orElse(null);
        this.minSalary = other.optionalMinSalary().map(LongFilter::copy).orElse(null);
        this.maxSalary = other.optionalMaxSalary().map(LongFilter::copy).orElse(null);
        this.taskId = other.optionalTaskId().map(LongFilter::copy).orElse(null);
        this.employeeId = other.optionalEmployeeId().map(LongFilter::copy).orElse(null);
        this.jobHistoryId = other.optionalJobHistoryId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public JobCriteria copy() {
        return new JobCriteria(this);
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

    public StringFilter getJobTitle() {
        return jobTitle;
    }

    public Optional<StringFilter> optionalJobTitle() {
        return Optional.ofNullable(jobTitle);
    }

    public StringFilter jobTitle() {
        if (jobTitle == null) {
            setJobTitle(new StringFilter());
        }
        return jobTitle;
    }

    public void setJobTitle(StringFilter jobTitle) {
        this.jobTitle = jobTitle;
    }

    public LongFilter getMinSalary() {
        return minSalary;
    }

    public Optional<LongFilter> optionalMinSalary() {
        return Optional.ofNullable(minSalary);
    }

    public LongFilter minSalary() {
        if (minSalary == null) {
            setMinSalary(new LongFilter());
        }
        return minSalary;
    }

    public void setMinSalary(LongFilter minSalary) {
        this.minSalary = minSalary;
    }

    public LongFilter getMaxSalary() {
        return maxSalary;
    }

    public Optional<LongFilter> optionalMaxSalary() {
        return Optional.ofNullable(maxSalary);
    }

    public LongFilter maxSalary() {
        if (maxSalary == null) {
            setMaxSalary(new LongFilter());
        }
        return maxSalary;
    }

    public void setMaxSalary(LongFilter maxSalary) {
        this.maxSalary = maxSalary;
    }

    public LongFilter getTaskId() {
        return taskId;
    }

    public Optional<LongFilter> optionalTaskId() {
        return Optional.ofNullable(taskId);
    }

    public LongFilter taskId() {
        if (taskId == null) {
            setTaskId(new LongFilter());
        }
        return taskId;
    }

    public void setTaskId(LongFilter taskId) {
        this.taskId = taskId;
    }

    public LongFilter getEmployeeId() {
        return employeeId;
    }

    public Optional<LongFilter> optionalEmployeeId() {
        return Optional.ofNullable(employeeId);
    }

    public LongFilter employeeId() {
        if (employeeId == null) {
            setEmployeeId(new LongFilter());
        }
        return employeeId;
    }

    public void setEmployeeId(LongFilter employeeId) {
        this.employeeId = employeeId;
    }

    public LongFilter getJobHistoryId() {
        return jobHistoryId;
    }

    public Optional<LongFilter> optionalJobHistoryId() {
        return Optional.ofNullable(jobHistoryId);
    }

    public LongFilter jobHistoryId() {
        if (jobHistoryId == null) {
            setJobHistoryId(new LongFilter());
        }
        return jobHistoryId;
    }

    public void setJobHistoryId(LongFilter jobHistoryId) {
        this.jobHistoryId = jobHistoryId;
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
        final JobCriteria that = (JobCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(jobTitle, that.jobTitle) &&
            Objects.equals(minSalary, that.minSalary) &&
            Objects.equals(maxSalary, that.maxSalary) &&
            Objects.equals(taskId, that.taskId) &&
            Objects.equals(employeeId, that.employeeId) &&
            Objects.equals(jobHistoryId, that.jobHistoryId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, jobTitle, minSalary, maxSalary, taskId, employeeId, jobHistoryId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JobCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalJobTitle().map(f -> "jobTitle=" + f + ", ").orElse("") +
            optionalMinSalary().map(f -> "minSalary=" + f + ", ").orElse("") +
            optionalMaxSalary().map(f -> "maxSalary=" + f + ", ").orElse("") +
            optionalTaskId().map(f -> "taskId=" + f + ", ").orElse("") +
            optionalEmployeeId().map(f -> "employeeId=" + f + ", ").orElse("") +
            optionalJobHistoryId().map(f -> "jobHistoryId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
