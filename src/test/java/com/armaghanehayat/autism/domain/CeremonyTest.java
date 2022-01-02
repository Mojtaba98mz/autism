package com.armaghanehayat.autism.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.armaghanehayat.autism.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CeremonyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ceremony.class);
        Ceremony ceremony1 = new Ceremony();
        ceremony1.setId(1L);
        Ceremony ceremony2 = new Ceremony();
        ceremony2.setId(ceremony1.getId());
        assertThat(ceremony1).isEqualTo(ceremony2);
        ceremony2.setId(2L);
        assertThat(ceremony1).isNotEqualTo(ceremony2);
        ceremony1.setId(null);
        assertThat(ceremony1).isNotEqualTo(ceremony2);
    }
}
