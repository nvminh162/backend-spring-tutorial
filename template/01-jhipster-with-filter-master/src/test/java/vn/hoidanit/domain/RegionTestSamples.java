package vn.hoidanit.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RegionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Region getRegionSample1() {
        return new Region().id(1L).regionName("regionName1");
    }

    public static Region getRegionSample2() {
        return new Region().id(2L).regionName("regionName2");
    }

    public static Region getRegionRandomSampleGenerator() {
        return new Region().id(longCount.incrementAndGet()).regionName(UUID.randomUUID().toString());
    }
}
