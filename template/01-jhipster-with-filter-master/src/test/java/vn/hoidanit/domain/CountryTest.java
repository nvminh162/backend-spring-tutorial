package vn.hoidanit.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static vn.hoidanit.domain.CountryTestSamples.*;
import static vn.hoidanit.domain.LocationTestSamples.*;
import static vn.hoidanit.domain.RegionTestSamples.*;

import org.junit.jupiter.api.Test;
import vn.hoidanit.web.rest.TestUtil;

class CountryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Country.class);
        Country country1 = getCountrySample1();
        Country country2 = new Country();
        assertThat(country1).isNotEqualTo(country2);

        country2.setId(country1.getId());
        assertThat(country1).isEqualTo(country2);

        country2 = getCountrySample2();
        assertThat(country1).isNotEqualTo(country2);
    }

    @Test
    void regionTest() {
        Country country = getCountryRandomSampleGenerator();
        Region regionBack = getRegionRandomSampleGenerator();

        country.setRegion(regionBack);
        assertThat(country.getRegion()).isEqualTo(regionBack);

        country.region(null);
        assertThat(country.getRegion()).isNull();
    }

    @Test
    void locationTest() {
        Country country = getCountryRandomSampleGenerator();
        Location locationBack = getLocationRandomSampleGenerator();

        country.setLocation(locationBack);
        assertThat(country.getLocation()).isEqualTo(locationBack);
        assertThat(locationBack.getCountry()).isEqualTo(country);

        country.location(null);
        assertThat(country.getLocation()).isNull();
        assertThat(locationBack.getCountry()).isNull();
    }
}
