package com.armaghanehayat.autism.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Ceremony.
 */
@Entity
@Table(name = "ceremony")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Ceremony implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "given_date")
    private Instant givenDate;

    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "receipt")
    private byte[] receipt;

    @Column(name = "receipt_content_type")
    private String receiptContentType;

    @ManyToOne
    @JsonIgnoreProperties(value = { "ceremonies" }, allowSetters = true)
    private CeremonyUser ceremonyUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Ceremony id(Long id) {
        this.id = id;
        return this;
    }

    public Long getAmount() {
        return this.amount;
    }

    public Ceremony amount(Long amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Instant getGivenDate() {
        return this.givenDate;
    }

    public Ceremony givenDate(Instant givenDate) {
        this.givenDate = givenDate;
        return this;
    }

    public void setGivenDate(Instant givenDate) {
        this.givenDate = givenDate;
    }

    public String getDescription() {
        return this.description;
    }

    public Ceremony description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getReceipt() {
        return this.receipt;
    }

    public Ceremony receipt(byte[] receipt) {
        this.receipt = receipt;
        return this;
    }

    public void setReceipt(byte[] receipt) {
        this.receipt = receipt;
    }

    public String getReceiptContentType() {
        return this.receiptContentType;
    }

    public Ceremony receiptContentType(String receiptContentType) {
        this.receiptContentType = receiptContentType;
        return this;
    }

    public void setReceiptContentType(String receiptContentType) {
        this.receiptContentType = receiptContentType;
    }

    public CeremonyUser getCeremonyUser() {
        return this.ceremonyUser;
    }

    public Ceremony ceremonyUser(CeremonyUser ceremonyUser) {
        this.setCeremonyUser(ceremonyUser);
        return this;
    }

    public void setCeremonyUser(CeremonyUser ceremonyUser) {
        this.ceremonyUser = ceremonyUser;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ceremony)) {
            return false;
        }
        return id != null && id.equals(((Ceremony) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ceremony{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", givenDate='" + getGivenDate() + "'" +
            ", description='" + getDescription() + "'" +
            ", receipt='" + getReceipt() + "'" +
            ", receiptContentType='" + getReceiptContentType() + "'" +
            "}";
    }
}
