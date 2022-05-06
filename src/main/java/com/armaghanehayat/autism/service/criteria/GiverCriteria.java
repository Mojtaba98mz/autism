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

    private StringFilter homeNumber;

    private StringFilter address;

    private InstantFilter absorbDate;

    private LongFilter provinceId;

    private StringFilter provinceName;

    private LongFilter cityId;

    private StringFilter cityName;

    private LongFilter donationId;

    private LongFilter giverauditorId;

    private LongFilter absorbantId;

    private LongFilter supporterId;

    private StringFilter supporterName;

    private StringFilter supporterFamily;

    private StringFilter absorbantName;

    private StringFilter absorbantFamily;

    private BooleanFilter disabled;

    public GiverCriteria() {}

    public GiverCriteria(GiverCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.family = other.family == null ? null : other.family.copy();
        this.phoneNumber = other.phoneNumber == null ? null : other.phoneNumber.copy();
        this.homeNumber = other.homeNumber == null ? null : other.homeNumber.copy();
        this.address = other.address == null ? null : other.address.copy();
        this.absorbDate = other.absorbDate == null ? null : other.absorbDate.copy();
        this.provinceId = other.provinceId == null ? null : other.provinceId.copy();
        this.provinceName = other.provinceId == null ? null : other.provinceName.copy();
        this.cityId = other.cityId == null ? null : other.cityId.copy();
        this.cityName = other.cityId == null ? null : other.cityName.copy();
        this.donationId = other.donationId == null ? null : other.donationId.copy();
        this.giverauditorId = other.giverauditorId == null ? null : other.giverauditorId.copy();
        this.absorbantId = other.absorbantId == null ? null : other.absorbantId.copy();
        this.supporterId = other.supporterId == null ? null : other.supporterId.copy();
        this.supporterName = other.supporterName == null ? null : other.supporterName.copy();
        this.supporterFamily = other.supporterFamily == null ? null : other.supporterFamily.copy();
        this.absorbantName = other.absorbantName == null ? null : other.absorbantName.copy();
        this.absorbantFamily = other.absorbantFamily == null ? null : other.absorbantFamily.copy();
        this.disabled = other.disabled == null ? null : other.disabled.copy();
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

    public StringFilter getProvinceName() {
        return provinceName;
    }

    public StringFilter getCityName() {
        return cityName;
    }

    public void setProvinceName(StringFilter provinceName) {
        this.provinceName = provinceName;
    }

    public void setCityName(StringFilter cityName) {
        this.cityName = cityName;
    }

    public LongFilter provinceId() {
        if (provinceId == null) {
            provinceId = new LongFilter();
        }
        return provinceId;
    }

    public StringFilter provinceName() {
        if (provinceName == null) {
            provinceName = new StringFilter();
        }
        return provinceName;
    }

    public StringFilter cityName() {
        if (cityName == null) {
            cityName = new StringFilter();
        }
        return cityName;
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

    public void setSupporterName(StringFilter supporterName) {
        this.supporterName = supporterName;
    }

    public StringFilter getSupporterName() {
        return supporterName;
    }

    public StringFilter supporterName() {
        if (supporterName == null) {
            supporterName = new StringFilter();
        }
        return supporterName;
    }

    public void setSupporterFamily(StringFilter supporterFamily) {
        this.supporterFamily = supporterFamily;
    }

    public StringFilter getSupporterFamily() {
        return supporterFamily;
    }

    public StringFilter supporterFamily() {
        if (supporterFamily == null) {
            supporterFamily = new StringFilter();
        }
        return supporterFamily;
    }

    public void setAbsorbantName(StringFilter absorbantName) {
        this.absorbantName = absorbantName;
    }

    public StringFilter getAbsorbantName() {
        return absorbantName;
    }

    public StringFilter absorbantName() {
        if (absorbantName == null) {
            absorbantName = new StringFilter();
        }
        return absorbantName;
    }

    public void setAbsorbantFamily(StringFilter absorbantFamily) {
        this.absorbantFamily = absorbantFamily;
    }

    public StringFilter getAbsorbantFamily() {
        return absorbantFamily;
    }

    public StringFilter absorbantFamily() {
        if (absorbantFamily == null) {
            absorbantFamily = new StringFilter();
        }
        return absorbantFamily;
    }

    public BooleanFilter getDisabled() {
        return disabled;
    }

    public BooleanFilter disabled() {
        if (disabled == null) {
            disabled = new BooleanFilter();
        }
        return disabled;
    }

    public void setDisabled(BooleanFilter disabled) {
        this.disabled = disabled;
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
            Objects.equals(homeNumber, that.homeNumber) &&
            Objects.equals(address, that.address) &&
            Objects.equals(absorbDate, that.absorbDate) &&
            Objects.equals(provinceId, that.provinceId) &&
            Objects.equals(cityId, that.cityId) &&
            Objects.equals(donationId, that.donationId) &&
            Objects.equals(giverauditorId, that.giverauditorId) &&
            Objects.equals(absorbantId, that.absorbantId) &&
            Objects.equals(supporterId, that.supporterId) &&
            Objects.equals(disabled, that.disabled) &&
            Objects.equals(supporterName, that.supporterName) &&
            Objects.equals(supporterFamily, that.supporterFamily) &&
            Objects.equals(absorbantName, that.absorbantName) &&
            Objects.equals(absorbantFamily, that.absorbantFamily)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            family,
            phoneNumber,
            homeNumber,
            address,
            absorbDate,
            provinceId,
            cityId,
            donationId,
            giverauditorId,
            absorbantId,
            supporterId,
            supporterName,
            supporterFamily,
            absorbantName,
            absorbantFamily,
            disabled
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
            (homeNumber != null ? "homeNumber=" + homeNumber + ", " : "") +
            (address != null ? "address=" + address + ", " : "") +
            (absorbDate != null ? "absorbDate=" + absorbDate + ", " : "") +
            (provinceId != null ? "provinceId=" + provinceId + ", " : "") +
            (cityId != null ? "cityId=" + cityId + ", " : "") +
            (disabled != null ? "disabled=" + disabled + ", " : "") +
            (donationId != null ? "donationId=" + donationId + ", " : "") +
            (giverauditorId != null ? "giverauditorId=" + giverauditorId + ", " : "") +
            (absorbantId != null ? "absorbantId=" + absorbantId + ", " : "") +
            (supporterId != null ? "supporterId=" + supporterId + ", " : "") +
            (supporterName != null ? "supporterName=" + supporterName + ", " : "") +
            (supporterFamily != null ? "supporterFamily=" + supporterFamily + ", " : "") +
            (absorbantName != null ? "absorbantName=" + absorbantName + ", " : "") +
            (absorbantFamily != null ? "absorbantFamily=" + absorbantFamily + ", " : "") +
            "}";
    }
}
