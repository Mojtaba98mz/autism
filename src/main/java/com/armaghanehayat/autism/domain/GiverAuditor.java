package com.armaghanehayat.autism.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A GiverAuditor.
 */
@Entity
@Table(name = "giver_auditor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class GiverAuditor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "fiedl_name", nullable = false)
    private String fiedlName;

    @NotNull
    @Column(name = "old_value", nullable = false)
    private String oldValue;

    @NotNull
    @Column(name = "new_value", nullable = false)
    private String newValue;

    @NotNull
    @Column(name = "change_date", nullable = false)
    private Instant changeDate;

    @ManyToOne
    private User auditor;

    @ManyToOne
    @JsonIgnoreProperties(value = { "donations", "giverauditors", "absorbant", "supporter", "province", "city" }, allowSetters = true)
    private Giver giver;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GiverAuditor id(Long id) {
        this.id = id;
        return this;
    }

    public String getFiedlName() {
        return this.fiedlName;
    }

    public GiverAuditor fiedlName(String fiedlName) {
        this.fiedlName = fiedlName;
        return this;
    }

    public void setFiedlName(String fiedlName) {
        this.fiedlName = fiedlName;
    }

    public String getOldValue() {
        return this.oldValue;
    }

    public GiverAuditor oldValue(String oldValue) {
        this.oldValue = oldValue;
        return this;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return this.newValue;
    }

    public GiverAuditor newValue(String newValue) {
        this.newValue = newValue;
        return this;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public Instant getChangeDate() {
        return this.changeDate;
    }

    public GiverAuditor changeDate(Instant changeDate) {
        this.changeDate = changeDate;
        return this;
    }

    public void setChangeDate(Instant changeDate) {
        this.changeDate = changeDate;
    }

    public User getAuditor() {
        return this.auditor;
    }

    public GiverAuditor auditor(User user) {
        this.setAuditor(user);
        return this;
    }

    public void setAuditor(User user) {
        this.auditor = user;
    }

    public Giver getGiver() {
        return this.giver;
    }

    public GiverAuditor giver(Giver giver) {
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
        if (!(o instanceof GiverAuditor)) {
            return false;
        }
        return id != null && id.equals(((GiverAuditor) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GiverAuditor{" +
            "id=" + getId() +
            ", fiedlName='" + getFiedlName() + "'" +
            ", oldValue='" + getOldValue() + "'" +
            ", newValue='" + getNewValue() + "'" +
            ", changeDate='" + getChangeDate() + "'" +
            "}";
    }
}
