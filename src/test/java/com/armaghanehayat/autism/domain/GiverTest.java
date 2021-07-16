package com.armaghanehayat.autism.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.armaghanehayat.autism.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GiverTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Giver.class);
        Giver giver1 = new Giver();
        giver1.setId(1L);
        Giver giver2 = new Giver();
        giver2.setId(giver1.getId());
        assertThat(giver1).isEqualTo(giver2);
        giver2.setId(2L);
        assertThat(giver1).isNotEqualTo(giver2);
        giver1.setId(null);
        assertThat(giver1).isNotEqualTo(giver2);
    }
}
