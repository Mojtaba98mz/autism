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
 * Criteria class for the {@link com.armaghanehayat.autism.domain.GiverAuditor} entity. This class is used
 * in {@link com.armaghanehayat.autism.web.rest.GiverAuditorResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /giver-auditors?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class GiverAuditorCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter fiedlName;

    private StringFilter oldValue;

    private StringFilter newValue;

    private InstantFilter changeDate;

    private LongFilter auditorId;

    private LongFilter giverId;

    public GiverAuditorCriteria() {}

    public GiverAuditorCriteria(GiverAuditorCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fiedlName = other.fiedlName == null ? null : other.fiedlName.copy();
        this.oldValue = other.oldValue == null ? null : other.oldValue.copy();
        this.newValue = other.newValue == null ? null : other.newValue.copy();
        this.changeDate = other.changeDate == null ? null : other.changeDate.copy();
        this.auditorId = other.auditorId == null ? null : other.auditorId.copy();
        this.giverId = other.giverId == null ? null : other.giverId.copy();
    }

    @Override
    public GiverAuditorCriteria copy() {
        return new GiverAuditorCriteria(this);
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

    public StringFilter getFiedlName() {
        return fiedlName;
    }

    public StringFilter fiedlName() {
        if (fiedlName == null) {
            fiedlName = new StringFilter();
        }
        return fiedlName;
    }

    public void setFiedlName(StringFilter fiedlName) {
        this.fiedlName = fiedlName;
    }

    public StringFilter getOldValue() {
        return oldValue;
    }

    public StringFilter oldValue() {
        if (oldValue == null) {
            oldValue = new StringFilter();
        }
        return oldValue;
    }

    public void setOldValue(StringFilter oldValue) {
        this.oldValue = oldValue;
    }

    public StringFilter getNewValue() {
        return newValue;
    }

    public StringFilter newValue() {
        if (newValue == null) {
            newValue = new StringFilter();
        }
        return newValue;
    }

    public void setNewValue(StringFilter newValue) {
        this.newValue = newValue;
    }

    public InstantFilter getChangeDate() {
        return changeDate;
    }

    public InstantFilter changeDate() {
        if (changeDate == null) {
            changeDate = new InstantFilter();
        }
        return changeDate;
    }

    public void setChangeDate(InstantFilter changeDate) {
        this.changeDate = changeDate;
    }

    public LongFilter getAuditorId() {
        return auditorId;
    }

    public LongFilter auditorId() {
        if (auditorId == null) {
            auditorId = new LongFilter();
        }
        return auditorId;
    }

    public void setAuditorId(LongFilter auditorId) {
        this.auditorId = auditorId;
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
        final GiverAuditorCriteria that = (GiverAuditorCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fiedlName, that.fiedlName) &&
            Objects.equals(oldValue, that.oldValue) &&
            Objects.equals(newValue, that.newValue) &&
            Objects.equals(changeDate, that.changeDate) &&
            Objects.equals(auditorId, that.auditorId) &&
            Objects.equals(giverId, that.giverId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fiedlName, oldValue, newValue, changeDate, auditorId, giverId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GiverAuditorCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (fiedlName != null ? "fiedlName=" + fiedlName + ", " : "") +
            (oldValue != null ? "oldValue=" + oldValue + ", " : "") +
            (newValue != null ? "newValue=" + newValue + ", " : "") +
            (changeDate != null ? "changeDate=" + changeDate + ", " : "") +
            (auditorId != null ? "auditorId=" + auditorId + ", " : "") +
            (giverId != null ? "giverId=" + giverId + ", " : "") +
            "}";
    }
}
