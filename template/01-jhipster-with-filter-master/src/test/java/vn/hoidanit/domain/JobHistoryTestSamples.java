package vn.hoidanit.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class JobHistoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static JobHistory getJobHistorySample1() {
        return new JobHistory().id(1L);
    }

    public static JobHistory getJobHistorySample2() {
        return new JobHistory().id(2L);
    }

    public static JobHistory getJobHistoryRandomSampleGenerator() {
        return new JobHistory().id(longCount.incrementAndGet());
    }
}
