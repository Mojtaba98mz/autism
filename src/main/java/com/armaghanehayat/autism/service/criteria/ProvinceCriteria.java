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
 * Criteria class for the {@link com.armaghanehayat.autism.domain.Province} entity. This class is used
 * in {@link com.armaghanehayat.autism.web.rest.ProvinceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /provinces?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProvinceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter enName;

    private LongFilter cityId;

    private LongFilter giverId;

    public ProvinceCriteria() {}

    public ProvinceCriteria(ProvinceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.enName = other.enName == null ? null : other.enName.copy();
        this.cityId = other.cityId == null ? null : other.cityId.copy();
        this.giverId = other.giverId == null ? null : other.giverId.copy();
    }

    @Override
    public ProvinceCriteria copy() {
        return new ProvinceCriteria(this);
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

    public StringFilter getEnName() {
        return enName;
    }

    public StringFilter enName() {
        if (enName == null) {
            enName = new StringFilter();
        }
        return enName;
    }

    public void setEnName(StringFilter enName) {
        this.enName = enName;
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
        final ProvinceCriteria that = (ProvinceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(enName, that.enName) &&
            Objects.equals(cityId, that.cityId) &&
            Objects.equals(giverId, that.giverId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, enName, cityId, giverId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProvinceCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (enName != null ? "enName=" + enName + ", " : "") +
            (cityId != null ? "cityId=" + cityId + ", " : "") +
            (giverId != null ? "giverId=" + giverId + ", " : "") +
            "}";
    }
}
