package vn.hoidanit.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link vn.hoidanit.domain.Employee} entity. This class is used
 * in {@link vn.hoidanit.web.rest.EmployeeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /employees?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmployeeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter email;

    private StringFilter phoneNumber;

    private InstantFilter hireDate;

    private LongFilter salary;

    private LongFilter commissionPct;

    private LongFilter jobId;

    private LongFilter managerId;

    private LongFilter departmentId;

    private LongFilter jobHistoryId;

    private Boolean distinct;

    public EmployeeCriteria() {}

    public EmployeeCriteria(EmployeeCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.firstName = other.optionalFirstName().map(StringFilter::copy).orElse(null);
        this.lastName = other.optionalLastName().map(StringFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.phoneNumber = other.optionalPhoneNumber().map(StringFilter::copy).orElse(null);
        this.hireDate = other.optionalHireDate().map(InstantFilter::copy).orElse(null);
        this.salary = other.optionalSalary().map(LongFilter::copy).orElse(null);
        this.commissionPct = other.optionalCommissionPct().map(LongFilter::copy).orElse(null);
        this.jobId = other.optionalJobId().map(LongFilter::copy).orElse(null);
        this.managerId = other.optionalManagerId().map(LongFilter::copy).orElse(null);
        this.departmentId = other.optionalDepartmentId().map(LongFilter::copy).orElse(null);
        this.jobHistoryId = other.optionalJobHistoryId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public EmployeeCriteria copy() {
        return new EmployeeCriteria(this);
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

    public StringFilter getFirstName() {
        return firstName;
    }

    public Optional<StringFilter> optionalFirstName() {
        return Optional.ofNullable(firstName);
    }

    public StringFilter firstName() {
        if (firstName == null) {
            setFirstName(new StringFilter());
        }
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public Optional<StringFilter> optionalLastName() {
        return Optional.ofNullable(lastName);
    }

    public StringFilter lastName() {
        if (lastName == null) {
            setLastName(new StringFilter());
        }
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getEmail() {
        return email;
    }

    public Optional<StringFilter> optionalEmail() {
        return Optional.ofNullable(email);
    }

    public StringFilter email() {
        if (email == null) {
            setEmail(new StringFilter());
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getPhoneNumber() {
        return phoneNumber;
    }

    public Optional<StringFilter> optionalPhoneNumber() {
        return Optional.ofNullable(phoneNumber);
    }

    public StringFilter phoneNumber() {
        if (phoneNumber == null) {
            setPhoneNumber(new StringFilter());
        }
        return phoneNumber;
    }

    public void setPhoneNumber(StringFilter phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public InstantFilter getHireDate() {
        return hireDate;
    }

    public Optional<InstantFilter> optionalHireDate() {
        return Optional.ofNullable(hireDate);
    }

    public InstantFilter hireDate() {
        if (hireDate == null) {
            setHireDate(new InstantFilter());
        }
        return hireDate;
    }

    public void setHireDate(InstantFilter hireDate) {
        this.hireDate = hireDate;
    }

    public LongFilter getSalary() {
        return salary;
    }

    public Optional<LongFilter> optionalSalary() {
        return Optional.ofNullable(salary);
    }

    public LongFilter salary() {
        if (salary == null) {
            setSalary(new LongFilter());
        }
        return salary;
    }

    public void setSalary(LongFilter salary) {
        this.salary = salary;
    }

    public LongFilter getCommissionPct() {
        return commissionPct;
    }

    public Optional<LongFilter> optionalCommissionPct() {
        return Optional.ofNullable(commissionPct);
    }

    public LongFilter commissionPct() {
        if (commissionPct == null) {
            setCommissionPct(new LongFilter());
        }
        return commissionPct;
    }

    public void setCommissionPct(LongFilter commissionPct) {
        this.commissionPct = commissionPct;
    }

    public LongFilter getJobId() {
        return jobId;
    }

    public Optional<LongFilter> optionalJobId() {
        return Optional.ofNullable(jobId);
    }

    public LongFilter jobId() {
        if (jobId == null) {
            setJobId(new LongFilter());
        }
        return jobId;
    }

    public void setJobId(LongFilter jobId) {
        this.jobId = jobId;
    }

    public LongFilter getManagerId() {
        return managerId;
    }

    public Optional<LongFilter> optionalManagerId() {
        return Optional.ofNullable(managerId);
    }

    public LongFilter managerId() {
        if (managerId == null) {
            setManagerId(new LongFilter());
        }
        return managerId;
    }

    public void setManagerId(LongFilter managerId) {
        this.managerId = managerId;
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
        final EmployeeCriteria that = (EmployeeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(email, that.email) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(hireDate, that.hireDate) &&
            Objects.equals(salary, that.salary) &&
            Objects.equals(commissionPct, that.commissionPct) &&
            Objects.equals(jobId, that.jobId) &&
            Objects.equals(managerId, that.managerId) &&
            Objects.equals(departmentId, that.departmentId) &&
            Objects.equals(jobHistoryId, that.jobHistoryId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            firstName,
            lastName,
            email,
            phoneNumber,
            hireDate,
            salary,
            commissionPct,
            jobId,
            managerId,
            departmentId,
            jobHistoryId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeeCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalFirstName().map(f -> "firstName=" + f + ", ").orElse("") +
            optionalLastName().map(f -> "lastName=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalPhoneNumber().map(f -> "phoneNumber=" + f + ", ").orElse("") +
            optionalHireDate().map(f -> "hireDate=" + f + ", ").orElse("") +
            optionalSalary().map(f -> "salary=" + f + ", ").orElse("") +
            optionalCommissionPct().map(f -> "commissionPct=" + f + ", ").orElse("") +
            optionalJobId().map(f -> "jobId=" + f + ", ").orElse("") +
            optionalManagerId().map(f -> "managerId=" + f + ", ").orElse("") +
            optionalDepartmentId().map(f -> "departmentId=" + f + ", ").orElse("") +
            optionalJobHistoryId().map(f -> "jobHistoryId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
