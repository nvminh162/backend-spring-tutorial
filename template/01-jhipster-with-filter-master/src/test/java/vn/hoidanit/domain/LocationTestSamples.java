package vn.hoidanit.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class LocationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Location getLocationSample1() {
        return new Location()
            .id(1L)
            .streetAddress("streetAddress1")
            .postalCode("postalCode1")
            .city("city1")
            .stateProvince("stateProvince1");
    }

    public static Location getLocationSample2() {
        return new Location()
            .id(2L)
            .streetAddress("streetAddress2")
            .postalCode("postalCode2")
            .city("city2")
            .stateProvince("stateProvince2");
    }

    public static Location getLocationRandomSampleGenerator() {
        return new Location()
            .id(longCount.incrementAndGet())
            .streetAddress(UUID.randomUUID().toString())
            .postalCode(UUID.randomUUID().toString())
            .city(UUID.randomUUID().toString())
            .stateProvince(UUID.randomUUID().toString());
    }
}
