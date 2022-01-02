package com.armaghanehayat.autism.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.armaghanehayat.autism.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CeremonyUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CeremonyUser.class);
        CeremonyUser ceremonyUser1 = new CeremonyUser();
        ceremonyUser1.setId(1L);
        CeremonyUser ceremonyUser2 = new CeremonyUser();
        ceremonyUser2.setId(ceremonyUser1.getId());
        assertThat(ceremonyUser1).isEqualTo(ceremonyUser2);
        ceremonyUser2.setId(2L);
        assertThat(ceremonyUser1).isNotEqualTo(ceremonyUser2);
        ceremonyUser1.setId(null);
        assertThat(ceremonyUser1).isNotEqualTo(ceremonyUser2);
    }
}
