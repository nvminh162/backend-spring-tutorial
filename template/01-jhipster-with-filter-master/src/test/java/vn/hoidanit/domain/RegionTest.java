package vn.hoidanit.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static vn.hoidanit.domain.CountryTestSamples.*;
import static vn.hoidanit.domain.RegionTestSamples.*;

import org.junit.jupiter.api.Test;
import vn.hoidanit.web.rest.TestUtil;

class RegionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Region.class);
        Region region1 = getRegionSample1();
        Region region2 = new Region();
        assertThat(region1).isNotEqualTo(region2);

        region2.setId(region1.getId());
        assertThat(region1).isEqualTo(region2);

        region2 = getRegionSample2();
        assertThat(region1).isNotEqualTo(region2);
    }

    @Test
    void countryTest() {
        Region region = getRegionRandomSampleGenerator();
        Country countryBack = getCountryRandomSampleGenerator();

        region.setCountry(countryBack);
        assertThat(region.getCountry()).isEqualTo(countryBack);
        assertThat(countryBack.getRegion()).isEqualTo(region);

        region.country(null);
        assertThat(region.getCountry()).isNull();
        assertThat(countryBack.getRegion()).isNull();
    }
}
