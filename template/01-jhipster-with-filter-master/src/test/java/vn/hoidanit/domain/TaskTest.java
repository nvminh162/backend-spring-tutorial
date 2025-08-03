package vn.hoidanit.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static vn.hoidanit.domain.JobTestSamples.*;
import static vn.hoidanit.domain.TaskTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import vn.hoidanit.web.rest.TestUtil;

class TaskTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Task.class);
        Task task1 = getTaskSample1();
        Task task2 = new Task();
        assertThat(task1).isNotEqualTo(task2);

        task2.setId(task1.getId());
        assertThat(task1).isEqualTo(task2);

        task2 = getTaskSample2();
        assertThat(task1).isNotEqualTo(task2);
    }

    @Test
    void jobTest() {
        Task task = getTaskRandomSampleGenerator();
        Job jobBack = getJobRandomSampleGenerator();

        task.addJob(jobBack);
        assertThat(task.getJobs()).containsOnly(jobBack);
        assertThat(jobBack.getTasks()).containsOnly(task);

        task.removeJob(jobBack);
        assertThat(task.getJobs()).doesNotContain(jobBack);
        assertThat(jobBack.getTasks()).doesNotContain(task);

        task.jobs(new HashSet<>(Set.of(jobBack)));
        assertThat(task.getJobs()).containsOnly(jobBack);
        assertThat(jobBack.getTasks()).containsOnly(task);

        task.setJobs(new HashSet<>());
        assertThat(task.getJobs()).doesNotContain(jobBack);
        assertThat(jobBack.getTasks()).doesNotContain(task);
    }
}
