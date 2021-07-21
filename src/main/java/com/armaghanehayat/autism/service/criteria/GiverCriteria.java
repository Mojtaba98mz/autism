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
 * Criteria class for the {@link com.armaghanehayat.autism.domain.Giver} entity. This class is used
 * in {@link com.armaghanehayat.autism.web.rest.GiverResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /givers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class GiverCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter family;

    private StringFilter phoneNumber;

    private StringFilter code;

    private StringFilter address;

    private InstantFilter absorbDate;

    private LongFilter provinceId;

    private LongFilter cityId;

    private LongFilter donationId;

    private LongFilter giverauditorId;

    private LongFilter absorbantId;

    private LongFilter supporterId;

    public GiverCriteria() {}

    public GiverCriteria(GiverCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.family = other.family == null ? null : other.family.copy();
        this.phoneNumber = other.phoneNumber == null ? null : other.phoneNumber.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.address = other.address == null ? null : other.address.copy();
        this.absorbDate = other.absorbDate == null ? null : other.absorbDate.copy();
        this.provinceId = other.provinceId == null ? null : other.provinceId.copy();
        this.cityId = other.cityId == null ? null : other.cityId.copy();
        this.donationId = other.donationId == null ? null : other.donationId.copy();
        this.giverauditorId = other.giverauditorId == null ? null : other.giverauditorId.copy();
        this.absorbantId = other.absorbantId == null ? null : other.absorbantId.copy();
        this.supporterId = other.supporterId == null ? null : other.supporterId.copy();
    }

    @Override
    public GiverCriteria copy() {
        return new GiverCriteria(this);
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

    public StringFilter getCode() {
        return code;
    }

    public StringFilter code() {
        if (code == null) {
            code = new StringFilter();
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
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

    public InstantFilter getAbsorbDate() {
        return absorbDate;
    }

    public InstantFilter absorbDate() {
        if (absorbDate == null) {
            absorbDate = new InstantFilter();
        }
        return absorbDate;
    }

    public void setAbsorbDate(InstantFilter absorbDate) {
        this.absorbDate = absorbDate;
    }

    public LongFilter getProvinceId() {
        return provinceId;
    }

    public LongFilter provinceId() {
        if (provinceId == null) {
            provinceId = new LongFilter();
        }
        return provinceId;
    }

    public void setProvinceId(LongFilter provinceId) {
        this.provinceId = provinceId;
    }

    public LongFilter getCityId() {
        return cityId;
    }

    public LongFilter cityId() {
        if (cityId == null) {
            cityId = new LongFilter();
        }
        return cityId;
    }

    public void setCityId(LongFilter cityId) {
        this.cityId = cityId;
    }

    public LongFilter getDonationId() {
        return donationId;
    }

    public LongFilter donationId() {
        if (donationId == null) {
            donationId = new LongFilter();
        }
        return donationId;
    }

    public void setDonationId(LongFilter donationId) {
        this.donationId = donationId;
    }

    public LongFilter getGiverauditorId() {
        return giverauditorId;
    }

    public LongFilter giverauditorId() {
        if (giverauditorId == null) {
            giverauditorId = new LongFilter();
        }
        return giverauditorId;
    }

    public void setGiverauditorId(LongFilter giverauditorId) {
        this.giverauditorId = giverauditorId;
    }

    public LongFilter getAbsorbantId() {
        return absorbantId;
    }

    public LongFilter absorbantId() {
        if (absorbantId == null) {
            absorbantId = new LongFilter();
        }
        return absorbantId;
    }

    public void setAbsorbantId(LongFilter absorbantId) {
        this.absorbantId = absorbantId;
    }

    public LongFilter getSupporterId() {
        return supporterId;
    }

    public LongFilter supporterId() {
        if (supporterId == null) {
            supporterId = new LongFilter();
        }
        return supporterId;
    }

    public void setSupporterId(LongFilter supporterId) {
        this.supporterId = supporterId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final GiverCriteria that = (GiverCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(family, that.family) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(code, that.code) &&
            Objects.equals(address, that.address) &&
            Objects.equals(absorbDate, that.absorbDate) &&
            Objects.equals(provinceId, that.provinceId) &&
            Objects.equals(cityId, that.cityId) &&
            Objects.equals(donationId, that.donationId) &&
            Objects.equals(giverauditorId, that.giverauditorId) &&
            Objects.equals(absorbantId, that.absorbantId) &&
            Objects.equals(supporterId, that.supporterId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            family,
            phoneNumber,
            code,
            address,
            absorbDate,
            provinceId,
            cityId,
            donationId,
            giverauditorId,
            absorbantId,
            supporterId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GiverCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (family != null ? "family=" + family + ", " : "") +
            (phoneNumber != null ? "phoneNumber=" + phoneNumber + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (address != null ? "address=" + address + ", " : "") +
            (absorbDate != null ? "absorbDate=" + absorbDate + ", " : "") +
            (provinceId != null ? "provinceId=" + provinceId + ", " : "") +
            (cityId != null ? "cityId=" + cityId + ", " : "") +
            (donationId != null ? "donationId=" + donationId + ", " : "") +
            (giverauditorId != null ? "giverauditorId=" + giverauditorId + ", " : "") +
            (absorbantId != null ? "absorbantId=" + absorbantId + ", " : "") +
            (supporterId != null ? "supporterId=" + supporterId + ", " : "") +
            "}";
    }
}
