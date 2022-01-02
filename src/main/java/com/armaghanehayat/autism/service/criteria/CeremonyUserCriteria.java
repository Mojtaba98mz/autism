package com.armaghanehayat.autism.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.armaghanehayat.autism.domain.CeremonyUser} entity. This class is used
 * in {@link com.armaghanehayat.autism.web.rest.CeremonyUserResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ceremony-users?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CeremonyUserCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter family;

    private StringFilter phoneNumber;

    private StringFilter homeNumber;

    private StringFilter address;

    private LongFilter ceremonyId;

    public CeremonyUserCriteria() {}

    public CeremonyUserCriteria(CeremonyUserCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.family = other.family == null ? null : other.family.copy();
        this.phoneNumber = other.phoneNumber == null ? null : other.phoneNumber.copy();
        this.homeNumber = other.homeNumber == null ? null : other.homeNumber.copy();
        this.address = other.address == null ? null : other.address.copy();
        this.ceremonyId = other.ceremonyId == null ? null : other.ceremonyId.copy();
    }

    @Override
    public CeremonyUserCriteria copy() {
        return new CeremonyUserCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getFamily() {
        return family;
    }

    public StringFilter family() {
        if (family == null) {
            family = new StringFilter();
        }
        return family;
    }

    public void setFamily(StringFilter family) {
        this.family = family;
    }

    public StringFilter getPhoneNumber() {
        return phoneNumber;
    }

    public StringFilter phoneNumber() {
        if (phoneNumber == null) {
            phoneNumber = new StringFilter();
        }
        return phoneNumber;
    }

    public void setPhoneNumber(StringFilter phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public StringFilter getHomeNumber() {
        return homeNumber;
    }

    public StringFilter homeNumber() {
        if (homeNumber == null) {
            homeNumber = new StringFilter();
        }
        return homeNumber;
    }

    public void setHomeNumber(StringFilter homeNumber) {
        this.homeNumber = homeNumber;
    }

    public StringFilter getAddress() {
        return address;
    }

    public StringFilter address() {
        if (address == null) {
            address = new StringFilter();
        }
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public LongFilter getCeremonyId() {
        return ceremonyId;
    }

    public LongFilter ceremonyId() {
        if (ceremonyId == null) {
            ceremonyId = new LongFilter();
        }
        return ceremonyId;
    }

    public void setCeremonyId(LongFilter ceremonyId) {
        this.ceremonyId = ceremonyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CeremonyUserCriteria that = (CeremonyUserCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(family, that.family) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(homeNumber, that.homeNumber) &&
            Objects.equals(address, that.address) &&
            Objects.equals(ceremonyId, that.ceremonyId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, family, phoneNumber, homeNumber, address, ceremonyId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CeremonyUserCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (family != null ? "family=" + family + ", " : "") +
            (phoneNumber != null ? "phoneNumber=" + phoneNumber + ", " : "") +
            (homeNumber != null ? "homeNumber=" + homeNumber + ", " : "") +
            (address != null ? "address=" + address + ", " : "") +
            (ceremonyId != null ? "ceremonyId=" + ceremonyId + ", " : "") +
            "}";
    }
}
