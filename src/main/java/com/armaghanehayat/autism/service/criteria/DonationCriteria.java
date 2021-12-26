package com.armaghanehayat.autism.service.criteria;

import com.armaghanehayat.autism.domain.enumeration.Account;
import com.armaghanehayat.autism.domain.enumeration.HelpType;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.armaghanehayat.autism.domain.Donation} entity. This class is used
 * in {@link com.armaghanehayat.autism.web.rest.DonationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /donations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DonationCriteria implements Serializable, Criteria {

    /**
     * Class for filtering HelpType
     */
    public static class HelpTypeFilter extends Filter<HelpType> {

        public HelpTypeFilter() {}

        public HelpTypeFilter(HelpTypeFilter filter) {
            super(filter);
        }

        @Override
        public HelpTypeFilter copy() {
            return new HelpTypeFilter(this);
        }
    }

    /**
     * Class for filtering Account
     */
    public static class AccountFilter extends Filter<Account> {

        public AccountFilter() {}

        public AccountFilter(AccountFilter filter) {
            super(filter);
        }

        @Override
        public AccountFilter copy() {
            return new AccountFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BooleanFilter isCash;

    private LongFilter amount;

    private InstantFilter donationDate;

    private HelpTypeFilter helpType;

    private StringFilter description;

    private AccountFilter account;

    private LongFilter giverId;

    public DonationCriteria() {}

    public DonationCriteria(DonationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.isCash = other.isCash == null ? null : other.isCash.copy();
        this.amount = other.amount == null ? null : other.amount.copy();
        this.donationDate = other.donationDate == null ? null : other.donationDate.copy();
        this.helpType = other.helpType == null ? null : other.helpType.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.account = other.account == null ? null : other.account.copy();
        this.giverId = other.giverId == null ? null : other.giverId.copy();
    }

    @Override
    public DonationCriteria copy() {
        return new DonationCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public BooleanFilter getIsCash() {
        return isCash;
    }

    public BooleanFilter isCash() {
        if (isCash == null) {
            isCash = new BooleanFilter();
        }
        return isCash;
    }

    public void setIsCash(BooleanFilter isCash) {
        this.isCash = isCash;
    }

    public LongFilter getAmount() {
        return amount;
    }

    public LongFilter amount() {
        if (amount == null) {
            amount = new LongFilter();
        }
        return amount;
    }

    public void setAmount(LongFilter amount) {
        this.amount = amount;
    }

    public InstantFilter getDonationDate() {
        return donationDate;
    }

    public InstantFilter donationDate() {
        if (donationDate == null) {
            donationDate = new InstantFilter();
        }
        return donationDate;
    }

    public void setDonationDate(InstantFilter donationDate) {
        this.donationDate = donationDate;
    }

    public HelpTypeFilter getHelpType() {
        return helpType;
    }

    public HelpTypeFilter helpType() {
        if (helpType == null) {
            helpType = new HelpTypeFilter();
        }
        return helpType;
    }

    public void setHelpType(HelpTypeFilter helpType) {
        this.helpType = helpType;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public AccountFilter getAccount() {
        return account;
    }

    public AccountFilter account() {
        if (account == null) {
            account = new AccountFilter();
        }
        return account;
    }

    public void setAccount(AccountFilter account) {
        this.account = account;
    }

    public LongFilter getGiverId() {
        return giverId;
    }

    public LongFilter giverId() {
        if (giverId == null) {
            giverId = new LongFilter();
        }
        return giverId;
    }

    public void setGiverId(LongFilter giverId) {
        this.giverId = giverId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DonationCriteria that = (DonationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(isCash, that.isCash) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(donationDate, that.donationDate) &&
            Objects.equals(helpType, that.helpType) &&
            Objects.equals(description, that.description) &&
            Objects.equals(account, that.account) &&
            Objects.equals(giverId, that.giverId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isCash, amount, donationDate, helpType, description, account, giverId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DonationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (isCash != null ? "isCash=" + isCash + ", " : "") +
            (amount != null ? "amount=" + amount + ", " : "") +
            (donationDate != null ? "donationDate=" + donationDate + ", " : "") +
            (helpType != null ? "helpType=" + helpType + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (account != null ? "account=" + account + ", " : "") +
            (giverId != null ? "giverId=" + giverId + ", " : "") +
            "}";
    }
}
