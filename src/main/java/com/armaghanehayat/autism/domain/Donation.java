package com.armaghanehayat.autism.domain;

import com.armaghanehayat.autism.domain.enumeration.HelpType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Donation.
 */
@Entity
@Table(name = "donation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Donation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "is_cash")
    private Boolean isCash;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "donation_date")
    private Instant donationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "help_type")
    private HelpType helpType;

    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "receipt")
    private byte[] receipt;

    @Column(name = "receipt_content_type")
    private String receiptContentType;

    @ManyToOne
    @JsonIgnoreProperties(value = { "province", "city", "donations", "giverauditors", "absorbant", "supporter" }, allowSetters = true)
    private Giver giver;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Donation id(Long id) {
        this.id = id;
        return this;
    }

    public Boolean getIsCash() {
        return this.isCash;
    }

    public Donation isCash(Boolean isCash) {
        this.isCash = isCash;
        return this;
    }

    public void setIsCash(Boolean isCash) {
        this.isCash = isCash;
    }

    public Long getAmount() {
        return this.amount;
    }

    public Donation amount(Long amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Instant getDonationDate() {
        return this.donationDate;
    }

    public Donation donationDate(Instant donationDate) {
        this.donationDate = donationDate;
        return this;
    }

    public void setDonationDate(Instant donationDate) {
        this.donationDate = donationDate;
    }

    public HelpType getHelpType() {
        return this.helpType;
    }

    public Donation helpType(HelpType helpType) {
        this.helpType = helpType;
        return this;
    }

    public void setHelpType(HelpType helpType) {
        this.helpType = helpType;
    }

    public String getDescription() {
        return this.description;
    }

    public Donation description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getReceipt() {
        return this.receipt;
    }

    public Donation receipt(byte[] receipt) {
        this.receipt = receipt;
        return this;
    }

    public void setReceipt(byte[] receipt) {
        this.receipt = receipt;
    }

    public String getReceiptContentType() {
        return this.receiptContentType;
    }

    public Donation receiptContentType(String receiptContentType) {
        this.receiptContentType = receiptContentType;
        return this;
    }

    public void setReceiptContentType(String receiptContentType) {
        this.receiptContentType = receiptContentType;
    }

    public Giver getGiver() {
        return this.giver;
    }

    public Donation giver(Giver giver) {
        this.setGiver(giver);
        return this;
    }

    public void setGiver(Giver giver) {
        this.giver = giver;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Donation)) {
            return false;
        }
        return id != null && id.equals(((Donation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Donation{" +
            "id=" + getId() +
            ", isCash='" + getIsCash() + "'" +
            ", amount=" + getAmount() +
            ", donationDate='" + getDonationDate() + "'" +
            ", helpType='" + getHelpType() + "'" +
            ", description='" + getDescription() + "'" +
            ", receipt='" + getReceipt() + "'" +
            ", receiptContentType='" + getReceiptContentType() + "'" +
            "}";
    }
}
