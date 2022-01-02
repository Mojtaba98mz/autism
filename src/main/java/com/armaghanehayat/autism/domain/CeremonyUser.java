package com.armaghanehayat.autism.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CeremonyUser.
 */
@Entity
@Table(name = "ceremony_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CeremonyUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "family", nullable = false)
    private String family;

    @NotNull
    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "home_number")
    private String homeNumber;

    @Column(name = "address")
    private String address;

    @OneToMany(mappedBy = "ceremonyUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ceremonyUser" }, allowSetters = true)
    private Set<Ceremony> ceremonies = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CeremonyUser id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public CeremonyUser name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamily() {
        return this.family;
    }

    public CeremonyUser family(String family) {
        this.family = family;
        return this;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public CeremonyUser phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getHomeNumber() {
        return this.homeNumber;
    }

    public CeremonyUser homeNumber(String homeNumber) {
        this.homeNumber = homeNumber;
        return this;
    }

    public void setHomeNumber(String homeNumber) {
        this.homeNumber = homeNumber;
    }

    public String getAddress() {
        return this.address;
    }

    public CeremonyUser address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<Ceremony> getCeremonies() {
        return this.ceremonies;
    }

    public CeremonyUser ceremonies(Set<Ceremony> ceremonies) {
        this.setCeremonies(ceremonies);
        return this;
    }

    public CeremonyUser addCeremony(Ceremony ceremony) {
        this.ceremonies.add(ceremony);
        ceremony.setCeremonyUser(this);
        return this;
    }

    public CeremonyUser removeCeremony(Ceremony ceremony) {
        this.ceremonies.remove(ceremony);
        ceremony.setCeremonyUser(null);
        return this;
    }

    public void setCeremonies(Set<Ceremony> ceremonies) {
        if (this.ceremonies != null) {
            this.ceremonies.forEach(i -> i.setCeremonyUser(null));
        }
        if (ceremonies != null) {
            ceremonies.forEach(i -> i.setCeremonyUser(this));
        }
        this.ceremonies = ceremonies;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CeremonyUser)) {
            return false;
        }
        return id != null && id.equals(((CeremonyUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CeremonyUser{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", family='" + getFamily() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", homeNumber='" + getHomeNumber() + "'" +
            ", address='" + getAddress() + "'" +
            "}";
    }
}
