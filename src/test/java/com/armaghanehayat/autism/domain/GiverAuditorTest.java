package com.armaghanehayat.autism.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.armaghanehayat.autism.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GiverAuditorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GiverAuditor.class);
        GiverAuditor giverAuditor1 = new GiverAuditor();
        giverAuditor1.setId(1L);
        GiverAuditor giverAuditor2 = new GiverAuditor();
        giverAuditor2.setId(giverAuditor1.getId());
        assertThat(giverAuditor1).isEqualTo(giverAuditor2);
        giverAuditor2.setId(2L);
        assertThat(giverAuditor1).isNotEqualTo(giverAuditor2);
        giverAuditor1.setId(null);
        assertThat(giverAuditor1).isNotEqualTo(giverAuditor2);
    }
}
