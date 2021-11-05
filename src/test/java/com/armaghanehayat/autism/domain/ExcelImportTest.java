package com.armaghanehayat.autism.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.armaghanehayat.autism.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExcelImportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExcelImport.class);
        ExcelImport excelImport1 = new ExcelImport();
        excelImport1.setId(1L);
        ExcelImport excelImport2 = new ExcelImport();
        excelImport2.setId(excelImport1.getId());
        assertThat(excelImport1).isEqualTo(excelImport2);
        excelImport2.setId(2L);
        assertThat(excelImport1).isNotEqualTo(excelImport2);
        excelImport1.setId(null);
        assertThat(excelImport1).isNotEqualTo(excelImport2);
    }
}
