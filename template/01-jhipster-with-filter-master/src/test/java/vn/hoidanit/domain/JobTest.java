package vn.hoidanit.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static vn.hoidanit.domain.EmployeeTestSamples.*;
import static vn.hoidanit.domain.JobHistoryTestSamples.*;
import static vn.hoidanit.domain.JobTestSamples.*;
import static vn.hoidanit.domain.TaskTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import vn.hoidanit.web.rest.TestUtil;

class JobTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Job.class);
        Job job1 = getJobSample1();
        Job job2 = new Job();
        assertThat(job1).isNotEqualTo(job2);

        job2.setId(job1.getId());
        assertThat(job1).isEqualTo(job2);

        job2 = getJobSample2();
        assertThat(job1).isNotEqualTo(job2);
    }

    @Test
    void taskTest() {
        Job job = getJobRandomSampleGenerator();
        Task taskBack = getTaskRandomSampleGenerator();

        job.addTask(taskBack);
        assertThat(job.getTasks()).containsOnly(taskBack);

        job.removeTask(taskBack);
        assertThat(job.getTasks()).doesNotContain(taskBack);

        job.tasks(new HashSet<>(Set.of(taskBack)));
        assertThat(job.getTasks()).containsOnly(taskBack);

        job.setTasks(new HashSet<>());
        assertThat(job.getTasks()).doesNotContain(taskBack);
    }

    @Test
    void employeeTest() {
        Job job = getJobRandomSampleGenerator();
        Employee employeeBack = getEmployeeRandomSampleGenerator();

        job.setEmployee(employeeBack);
        assertThat(job.getEmployee()).isEqualTo(employeeBack);

        job.employee(null);
        assertThat(job.getEmployee()).isNull();
    }

    @Test
    void jobHistoryTest() {
        Job job = getJobRandomSampleGenerator();
        JobHistory jobHistoryBack = getJobHistoryRandomSampleGenerator();

        job.setJobHistory(jobHistoryBack);
        assertThat(job.getJobHistory()).isEqualTo(jobHistoryBack);
        assertThat(jobHistoryBack.getJob()).isEqualTo(job);

        job.jobHistory(null);
        assertThat(job.getJobHistory()).isNull();
        assertThat(jobHistoryBack.getJob()).isNull();
    }
}
