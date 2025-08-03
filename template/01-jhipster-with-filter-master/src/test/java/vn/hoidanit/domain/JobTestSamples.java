package vn.hoidanit.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class JobTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Job getJobSample1() {
        return new Job().id(1L).jobTitle("jobTitle1").minSalary(1L).maxSalary(1L);
    }

    public static Job getJobSample2() {
        return new Job().id(2L).jobTitle("jobTitle2").minSalary(2L).maxSalary(2L);
    }

    public static Job getJobRandomSampleGenerator() {
        return new Job()
            .id(longCount.incrementAndGet())
            .jobTitle(UUID.randomUUID().toString())
            .minSalary(longCount.incrementAndGet())
            .maxSalary(longCount.incrementAndGet());
    }
}
