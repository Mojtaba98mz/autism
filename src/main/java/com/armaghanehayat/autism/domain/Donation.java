package com.armaghanehayat.autism.domain;

import com.armaghanehayat.autism.domain.enumeration.Account;
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
    @Column(name = "id")
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

    @Enumerated(EnumType.STRING)
    @Column(name = "account")
    private Account account;

    @Column(name = "register_date")
    private Instant registerDate;

    @ManyToOne
    @JsonIgnoreProperties(value = { "donations", "giverauditors", "absorbant", "supporter", "province", "city" }, allowSetters = true)
    private Giver giver;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Donation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIsCash() {
        return this.isCash;
    }

    public Donation isCash(Boolean isCash) {
        this.setIsCash(isCash);
        return this;
    }

    public void setIsCash(Boolean isCash) {
        this.isCash = isCash;
    }

    public Long getAmount() {
        return this.amount;
    }

    public Donation amount(Long amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Instant getDonationDate() {
        return this.donationDate;
    }

    public Donation donationDate(Instant donationDate) {
        this.setDonationDate(donationDate);
        return this;
    }

    public void setDonationDate(Instant donationDate) {
        this.donationDate = donationDate;
    }

    public HelpType getHelpType() {
        return this.helpType;
    }

    public Donation helpType(HelpType helpType) {
        this.setHelpType(helpType);
        return this;
    }

    public void setHelpType(HelpType helpType) {
        this.helpType = helpType;
    }

    public String getDescription() {
        return this.description;
    }

    public Donation description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getReceipt() {
        return this.receipt;
    }

    public Donation receipt(byte[] receipt) {
        this.setReceipt(receipt);
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

    public Account getAccount() {
        return this.account;
    }

    public Donation account(Account account) {
        this.setAccount(account);
        return this;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Instant getRegisterDate() {
        return this.registerDate;
    }

    public Donation registerDate(Instant registerDate) {
        this.setRegisterDate(registerDate);
        return this;
    }

    public void setRegisterDate(Instant registerDate) {
        this.registerDate = registerDate;
    }

    public Giver getGiver() {
        return this.giver;
    }

    public void setGiver(Giver giver) {
        this.giver = giver;
    }

    public Donation giver(Giver giver) {
        this.setGiver(giver);
        return this;
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
            ", account='" + getAccount() + "'" +
            ", registerDate='" + getRegisterDate() + "'" +
            "}";
    }
}
