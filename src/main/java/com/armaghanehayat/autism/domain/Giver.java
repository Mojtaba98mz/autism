package com.armaghanehayat.autism.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Giver.
 */
@Entity
@Table(name = "giver")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Giver implements Serializable {

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

    @Column(name = "absorb_date")
    private Instant absorbDate;

    @OneToMany(mappedBy = "giver")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "giver" }, allowSetters = true)
    private Set<Donation> donations = new HashSet<>();

    @OneToMany(mappedBy = "giver")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "auditor", "giver" }, allowSetters = true)
    private Set<GiverAuditor> giverauditors = new HashSet<>();

    @ManyToOne
    private User absorbant;

    @ManyToOne
    private User supporter;

    @ManyToOne
    @JsonIgnoreProperties(value = { "cities", "givers" }, allowSetters = true)
    private Province province;

    @ManyToOne
    @JsonIgnoreProperties(value = { "givers", "province" }, allowSetters = true)
    private City city;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Giver id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Giver name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamily() {
        return this.family;
    }

    public Giver family(String family) {
        this.family = family;
        return this;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Giver phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getHomeNumber() {
        return this.homeNumber;
    }

    public Giver homeNumber(String homeNumber) {
        this.homeNumber = homeNumber;
        return this;
    }

    public void setHomeNumber(String homeNumber) {
        this.homeNumber = homeNumber;
    }

    public String getAddress() {
        return this.address;
    }

    public Giver address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Instant getAbsorbDate() {
        return this.absorbDate;
    }

    public Giver absorbDate(Instant absorbDate) {
        this.absorbDate = absorbDate;
        return this;
    }

    public void setAbsorbDate(Instant absorbDate) {
        this.absorbDate = absorbDate;
    }

    public Set<Donation> getDonations() {
        return this.donations;
    }

    public Giver donations(Set<Donation> donations) {
        this.setDonations(donations);
        return this;
    }

    public Giver addDonation(Donation donation) {
        this.donations.add(donation);
        donation.setGiver(this);
        return this;
    }

    public Giver removeDonation(Donation donation) {
        this.donations.remove(donation);
        donation.setGiver(null);
        return this;
    }

    public void setDonations(Set<Donation> donations) {
        if (this.donations != null) {
            this.donations.forEach(i -> i.setGiver(null));
        }
        if (donations != null) {
            donations.forEach(i -> i.setGiver(this));
        }
        this.donations = donations;
    }

    public Set<GiverAuditor> getGiverauditors() {
        return this.giverauditors;
    }

    public Giver giverauditors(Set<GiverAuditor> giverAuditors) {
        this.setGiverauditors(giverAuditors);
        return this;
    }

    public Giver addGiverauditor(GiverAuditor giverAuditor) {
        this.giverauditors.add(giverAuditor);
        giverAuditor.setGiver(this);
        return this;
    }

    public Giver removeGiverauditor(GiverAuditor giverAuditor) {
        this.giverauditors.remove(giverAuditor);
        giverAuditor.setGiver(null);
        return this;
    }

    public void setGiverauditors(Set<GiverAuditor> giverAuditors) {
        if (this.giverauditors != null) {
            this.giverauditors.forEach(i -> i.setGiver(null));
        }
        if (giverAuditors != null) {
            giverAuditors.forEach(i -> i.setGiver(this));
        }
        this.giverauditors = giverAuditors;
    }

    public User getAbsorbant() {
        return this.absorbant;
    }

    public Giver absorbant(User user) {
        this.setAbsorbant(user);
        return this;
    }

    public void setAbsorbant(User user) {
        this.absorbant = user;
    }

    public User getSupporter() {
        return this.supporter;
    }

    public Giver supporter(User user) {
        this.setSupporter(user);
        return this;
    }

    public void setSupporter(User user) {
        this.supporter = user;
    }

    public Province getProvince() {
        return this.province;
    }

    public Giver province(Province province) {
        this.setProvince(province);
        return this;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public City getCity() {
        return this.city;
    }

    public Giver city(City city) {
        this.setCity(city);
        return this;
    }

    public void setCity(City city) {
        this.city = city;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Giver)) {
            return false;
        }
        return id != null && id.equals(((Giver) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Giver{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", family='" + getFamily() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", homeNumber='" + getHomeNumber() + "'" +
            ", address='" + getAddress() + "'" +
            ", absorbDate='" + getAbsorbDate() + "'" +
            "}";
    }
}
