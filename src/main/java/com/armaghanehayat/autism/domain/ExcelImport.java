package com.armaghanehayat.autism.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ExcelImport.
 */
@Entity
@Table(name = "excel_import")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ExcelImport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Lob
    @Column(name = "excel", nullable = false)
    private byte[] excel;

    @Column(name = "excel_content_type", nullable = false)
    private String excelContentType;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ExcelImport id(Long id) {
        this.id = id;
        return this;
    }

    public byte[] getExcel() {
        return this.excel;
    }

    public ExcelImport excel(byte[] excel) {
        this.excel = excel;
        return this;
    }

    public void setExcel(byte[] excel) {
        this.excel = excel;
    }

    public String getExcelContentType() {
        return this.excelContentType;
    }

    public ExcelImport excelContentType(String excelContentType) {
        this.excelContentType = excelContentType;
        return this;
    }

    public void setExcelContentType(String excelContentType) {
        this.excelContentType = excelContentType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExcelImport)) {
            return false;
        }
        return id != null && id.equals(((ExcelImport) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExcelImport{" +
            "id=" + getId() +
            ", excel='" + getExcel() + "'" +
            ", excelContentType='" + getExcelContentType() + "'" +
            "}";
    }
}
