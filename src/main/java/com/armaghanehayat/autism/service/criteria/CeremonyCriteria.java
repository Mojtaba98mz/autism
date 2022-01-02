package com.armaghanehayat.autism.service.criteria;

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
 * Criteria class for the {@link com.armaghanehayat.autism.domain.Ceremony} entity. This class is used
 * in {@link com.armaghanehayat.autism.web.rest.CeremonyResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ceremonies?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CeremonyCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter amount;

    private InstantFilter givenDate;

    private StringFilter description;

    private LongFilter ceremonyUserId;

    public CeremonyCriteria() {}

    public CeremonyCriteria(CeremonyCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.amount = other.amount == null ? null : other.amount.copy();
        this.givenDate = other.givenDate == null ? null : other.givenDate.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.ceremonyUserId = other.ceremonyUserId == null ? null : other.ceremonyUserId.copy();
    }

    @Override
    public CeremonyCriteria copy() {
        return new CeremonyCriteria(this);
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

    public InstantFilter getGivenDate() {
        return givenDate;
    }

    public InstantFilter givenDate() {
        if (givenDate == null) {
            givenDate = new InstantFilter();
        }
        return givenDate;
    }

    public void setGivenDate(InstantFilter givenDate) {
        this.givenDate = givenDate;
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

    public LongFilter getCeremonyUserId() {
        return ceremonyUserId;
    }

    public LongFilter ceremonyUserId() {
        if (ceremonyUserId == null) {
            ceremonyUserId = new LongFilter();
        }
        return ceremonyUserId;
    }

    public void setCeremonyUserId(LongFilter ceremonyUserId) {
        this.ceremonyUserId = ceremonyUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CeremonyCriteria that = (CeremonyCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(givenDate, that.givenDate) &&
            Objects.equals(description, that.description) &&
            Objects.equals(ceremonyUserId, that.ceremonyUserId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, givenDate, description, ceremonyUserId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CeremonyCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (amount != null ? "amount=" + amount + ", " : "") +
            (givenDate != null ? "givenDate=" + givenDate + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (ceremonyUserId != null ? "ceremonyUserId=" + ceremonyUserId + ", " : "") +
            "}";
    }
}
